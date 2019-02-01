package com.thingtek.config.agreement;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.com.entity.BaseG2S;
import com.thingtek.com.entity.BaseS2G;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public @Data
class AgreementConfig extends BaseService {

    //    private byte[] net;
    private byte[] head;
    private int headoff;
    private byte[] seq;
    private int seqoff;
    private byte[] ordertype;
    private int ordertypeoff;
    private byte[] cmdtype;
    private int cmdtypeoff;
    private byte[] datalength;
    private int datalengthoff;
    private int onedatalength;
    private int parameteroff;
    private int totallength;


    private byte[] orderrequest;
    private byte[] orderresponse;

    private Map<Integer, String> G2S;
    private Map<Integer, String> S2G;

    private Map<String, BaseS2G> s2gmap;

    private List<DataAgreements> dataAgreements;
    private Map<Byte, UnitAgreements> unitAgreements;

    /*
        获取数据头下标
     */
    public int getHeadIndex(byte[] bytes) {
        int off = -1;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            if (bytes[i] == head[j]) {
                off = i;
                j++;
            } else {
                j = 0;
            }
            if (j >= head.length) {
                break;
            }
        }
        return off - head.length + 1;
    }


    public synchronized BaseG2S getG2S(byte[] bytes) {
        int cmds = 0;
        for (int i = 0; i < this.cmdtype.length; i++) {
            cmds |= (bytes[cmdtype[i]] & 0xff) << (i * 8);
        }
//        System.out.println(Integer.toHexString(cmds));
        try {
            BaseG2S g2s = (BaseG2S) this.getClass()
                    .getClassLoader()
                    .loadClass(G2S.get(cmds))
                    .newInstance();
            g2s.setAgreementConfig(this);
            return g2s.setBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized BaseS2G getS2G(String keycmd) {
        BaseS2G s2g = s2gmap.get(keycmd);
        s2g.setAgreementConfig(this);
        return s2g;
    }

    public int getDataLength(int off, byte[] bytes) {
        int dataLength = 0;
        for (int i = 0; i < this.datalength.length; i++) {
            dataLength |= (bytes[off + datalength[i]] & 0xff) << (i * 8);
        }
        return dataLength;
    }

    public int getOrderType(int off, byte[] bytes) {
        int order = 0;
        for (int i = 0; i < this.ordertype.length; i++) {
            order |= (bytes[off + ordertype[i]] & 0xff) << (i * 8);
        }
        return order;
    }

    public int getCmd(int off, byte[] bytes) {
        int cmd = 0;
        for (int i = 0; i < this.cmdtype.length; i++) {
            cmd |= (bytes[off + cmdtype[i]] & 0xff) << (i * 8);
        }
        return cmd;
    }

}
