package com.thingtek.serialPort;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.service.DataService;
import com.thingtek.beanServiceDao.net.service.NetService;
import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import com.thingtek.beanServiceDao.proinfo.service.ProInfoService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.com.entity.BaseG2S;
import com.thingtek.config.agreement.AgreementConfig;
import com.thingtek.serialException.NoSuchPort;
import com.thingtek.serialException.NotASerialPort;
import com.thingtek.serialException.PortInUse;
import com.thingtek.serialException.SerialPortParameterFailure;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.debugs.Debugs;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Component
public class SerialConnect extends BaseService {
    @Resource
    private SerialTool serialTool;
    @Resource
    private AgreementConfig agreementConfig;
    @Resource
    private Debugs debugs;

    private InputStream in;

    private OutputStream out;

    private boolean readflag = true;
    @Resource
    private LogoInfo logoInfo;
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private UnitService unitService;
    @Resource
    private DataService dataService;
    @Resource
    private NetService netService;

    private boolean connectflag;

    public boolean isConnectflag() {
        return connectflag;
    }

    private SerialPort port;

    public void openConnect() {
        ProInfoBean proinfo = proInfoService.getInfo();

        String com = proinfo.getCom();
        try {
            port = serialTool.openPort(com, 9600);
            in = port.getInputStream();
            out = port.getOutputStream();
            connectflag = true;
        } catch (IOException e) {
            log(e);
        } catch (PortInUse | NoSuchPort | NotASerialPort | SerialPortParameterFailure | NoSuchPortException | PortInUseException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            log(e);
        }
        read();
    }

    private int seq = 1;

    public synchronized void send(byte... bytes) {
        if (!connectflag) {
            openConnect();
        }
        try {
            int seqoff = agreementConfig.getSeqoff();
            int seqlength = agreementConfig.getSeq().length;
            System.arraycopy(int2bytes(seq++, seqlength), 0, bytes, seqoff, seqlength);
            calcCRC16_X(bytes);
            debugs.send(bytes, bytes.length, "");
            out.write(bytes);
        } catch (Exception e) {
            log(e);
        }
    }

    private byte[] caches;

    public void read() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = new byte[1024 * 1024];
                int commit = -1;
                try {
                    while ((commit = in.read(bytes)) != -1) {
                        if (!readflag) {
                            return;
                        }
                        if (commit == 0) {
                            continue;
                        }
                        //获取读取的数据
                        byte[] bs = new byte[commit];
                        System.arraycopy(bytes, 0, bs, 0, commit);
                        debugs.rec(bs, bs.length, Calendar.getInstance().getTime(), "");
                        if (caches == null || caches.length == 0) {
                            //缓存为空
                            caches = bs;
                        } else {
                            //缓存不为空
                            int beforelength = caches.length;
                            caches = Arrays.copyOf(caches, caches.length + commit);
                            System.arraycopy(bs, 0, caches, beforelength, commit);
                        }
                        if (caches.length < agreementConfig.getTotallength()) {//缓存的长度小于空数据长度
                            continue;
                        }
                        resolveCache();
                    }
                } catch (IOException e) {
                    log(e);
                }
            }
        }).start();
    }

    private void resolveCache() {
        int headOff = agreementConfig.getHeadIndex(caches);//获取协议头
        if (headOff == -1) {
            //缓存数据没有协议头 丢弃
            caches = null;
            return;
        }
        int datalength = agreementConfig.getDataLength(headOff, caches);//获取数据长度
        if (caches.length < headOff + datalength + agreementConfig.getTotallength()) {
            //缓存数据小于正常长度
            return;
        }

        //获取正常数据
        byte[] resultbytes = new byte[datalength + agreementConfig.getTotallength()];
        System.arraycopy(caches, headOff, resultbytes, 0, resultbytes.length);

        //校验
        boolean flag = checkCRC16_X(resultbytes);
        if (flag) saveData(resultbytes);
        //获取数据后缓存剩余
        if (caches.length > headOff + datalength + agreementConfig.getTotallength()) {
            //有剩余
            byte[] lastbytes = new byte[caches.length - resultbytes.length - headOff];
            System.arraycopy(caches, headOff + resultbytes.length, lastbytes, 0, lastbytes.length);
            caches = lastbytes;
            resolveCache();
        } else {
            //无剩余
            caches = null;
        }

    }

    private void saveData(byte[] resultbytes) {
//        dataBuffer.receDatas(new RawData(resultbytes, Calendar.getInstance().getTime()));
        BaseG2S g2s = agreementConfig.getG2S(resultbytes);
        if (g2s == null) {
            return;
        }
        g2s.setNetService(netService);
        g2s.setUnitService(unitService);
        g2s.setDataService(dataService);
        g2s.setProInfoService(proInfoService);
        g2s.setLogoInfo(logoInfo);
        g2s.resolve();

        byte[] bytes = g2s.getResult();
        if (bytes != null && bytes.length != 0) {
            send(bytes);
        }
    }

    public void closeConnect() {
        if (!connectflag) {
            return;
        }
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            log(e);
        }
        connectflag = false;
    }

    /* private int cal_serv_crc(byte[] message, int len) {
         int crc = 0;
         for (int i = 0; i < len; i++) {
             if (i % 2 == 0) {
                 crc += (message[i] & 0xff) << 8;
             } else
                 crc += message[i] & 0xff;
         }
         return crc;
     }*/
    private synchronized int cal_serv_crc(byte[] message, int len) {
        int crc = 0x00;
        int polynomial = 0x1021;
        for (int index = 0; index < len; index++) {
            byte b = message[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }


    protected byte[] calcCRC16_X(byte[] b) {
        int crc16 = cal_serv_crc(b, b.length - 2);
        b[b.length - 1] = (byte) (crc16 & 0xFF);
        b[b.length - 2] = (byte) (crc16 >> 8 & 0xFF);
        return b;
    }

    protected boolean checkCRC16_X(byte[] bytes) {
        int crc16 = cal_serv_crc(bytes, bytes.length - 2);
//        System.out.print("\n校验：" + Integer.toHexString(crc16));
        return (bytes[bytes.length - 1] == (byte) (crc16 & 0xFF))
                && (bytes[bytes.length - 2] == (byte) (crc16 >> 8 & 0xFF));
    }

    private int bytes2Int(byte b[], int off, int len) {
        int temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp << 8 | (b[i] & 0xFF);
        }
        return temp;
    }

    protected byte[] int2bytes(int i, int length) {
        byte[] bytes = new byte[length];
        for (int j = 0; j < length; j++) {
            bytes[j] = (byte) (i >> (length - 1 - j) * 8);
        }
        return bytes;
    }

}
