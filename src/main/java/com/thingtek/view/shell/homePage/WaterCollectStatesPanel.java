package com.thingtek.view.shell.homePage;

import com.thingtek.beanServiceDao.data.entity.WaterDataBean;
import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import com.thingtek.beanServiceDao.proinfo.service.ProInfoService;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.WaterUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.com.entity.BaseS2G;
import com.thingtek.config.agreement.AgreementConfig;
import com.thingtek.serialPort.SerialConnect;
import com.thingtek.view.component.panel.WaterCollectStatesUnitPanel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

@Component
public class WaterCollectStatesPanel extends BasePanel {

    public WaterCollectStatesPanel() {
        authority = UserService.UP;
    }

    @Resource
    private UnitService unitService;

    private byte unit_type = 1;

    @Override
    public WaterCollectStatesPanel init() {
        setOpaque(false);
        setLayout(new BorderLayout());
        initTop();
        initCenter();
        return this;
    }

    @Resource
    private ProInfoService proInfoService;
    @Resource
    private SerialConnect connect;
    @Resource
    private AgreementConfig agreementConfig;

    private boolean collect;

    public boolean isCollect() {
        return collect;
    }

    private void initTop() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        final JButton jbconnect = new JButton("开始测量");
        jbconnect.setOpaque(false);
        jbconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopCollect();
                if (waterDataCollectPanel.isCollect()) {
                    setInit();
                } else {
                    collect = true;
                    testConnect();
                }
//                setInit();
            }
        });
        top.add(jbconnect);
        this.add(top, BorderLayout.SOUTH);
    }

    private int connectTimes = 1;
    private Timer connectTimer;
    private Timer collectTimer;
    private Hashtable<Byte, Timer> startUnits = new Hashtable<>();
    private Hashtable<Byte, Boolean> startFlags = new Hashtable<>();
    private Hashtable<Byte, Integer> startTimes = new Hashtable<>();
    @Resource
    private WaterDataCollectPanel waterDataCollectPanel;

    private void testConnect() {

        connectTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                BaseS2G s2g = agreementConfig.getS2G("waternetconnect");
                byte[] data = new byte[]{0, 1};//单一网关
                s2g.setDatas(data);
                s2g.resolve();
                connect.send(s2g.getResult());
                connectTimes++;
                if (connectTimes >= 4) {
                    falseMessage("连接超时,请检查设备连接!");
                    connectTimes = 1;
                    collect = false;
                    connectTimer.cancel();
                }
            }
        };
        connectTimer.schedule(task, 0, 1000);
    }

    public void stopCollect() {
        connectTimes = 1;
        collect = false;
        if (connectTimer != null) {
            connectTimer.cancel();
            connectTimer = null;
        }
        if (collectTimer != null) {
            collectTimer.cancel();
            collectTimer = null;
        }
        for (Timer entry : startUnits.values()) {
            entry.cancel();
        }
        startUnits.clear();
        startFlags.clear();
        startTimes.clear();
    }

    public void stopTestConnect() {
        connectTimes = 1;
        if (connectTimer != null) {
            connectTimer.cancel();
            connectTimer = null;
        }
    }

    public void setInit() {
        ProInfoBean proInfoBean = proInfoService.getInfo();
        BaseS2G s2g = agreementConfig.getS2G("watersetinit");
        java.util.List<BaseUnitBean> units = unitService.getUnitsByUnitType(unit_type);
        byte[] bytes = new byte[2 + 4 + 7 * units.size()];
        int index = 0;
        bytes[index++] = 0;
        bytes[index++] = 1;//单一网关
        double vertical = proInfoBean.getVertical_value();
        byte[] vertical_bytes = float2Bytes((float) vertical);
        System.arraycopy(vertical_bytes, 0, bytes, index, vertical_bytes.length);
        index += 4;
        for (BaseUnitBean unit : units) {
            WaterUnitBean waterunit = (WaterUnitBean) unit;
//                    bytes[index++] = waterunit.getUnit_type();
            bytes[index++] = 0;
            bytes[index++] = waterunit.getUnit_num();
            float init = waterunit.getValue_init() == null ? 0 : waterunit.getValue_init();
            byte[] init_bytes = float2Bytes(init);
            System.arraycopy(init_bytes, 0, bytes, index, init_bytes.length);
            index += 4;
            bytes[index++] = (byte) (waterunit.getFrequency() * 10);
        }
        s2g.setDatas(bytes);
        s2g.resolve();
        connect.send(s2g.getResult());
    }


    public void collect() {
        connectTimes = 1;
        if (collectTimer != null) {
            collectTimer.cancel();
            collectTimer = null;
        }
        BaseS2G s2g = agreementConfig.getS2G("wateruploaddata");
        java.util.List<BaseUnitBean> units = unitService.getUnitsByUnitType(unit_type);
        int index = 0;
        byte[] datas = new byte[units.size() * 2 + 2];
        datas[index++] = 0;
        datas[index++] = 1;//单一网关
        for (BaseUnitBean unit : units) {
//            datas[index++] = unit.getUnit_type();
            datas[index++] = 0;
            datas[index++] = unit.getUnit_num();
        }
        s2g.setDatas(datas);
        s2g.resolve();
        final byte[] bytes = s2g.getResult();
        collectTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                connect.send(bytes);
            }
        };
        collectTimer.schedule(task, 0, 2000);

    }

    private byte[] float2Bytes(float f) {
        byte[] bytes = new byte[4];
        int i = Float.floatToIntBits(f);
//         System.out.println(i);
        for (int j = 0; j < bytes.length; j++) {
            bytes[j] = (byte) ((i >> ((bytes.length - j - 1) * 8)) & 0xff);
        }
        return bytes;
    }

    private Map<Byte, WaterCollectStatesUnitPanel> panels;
    private JPanel center;

    private void initCenter() {
        center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BorderLayout());
        initCenterUnit();
        JScrollPane jScrollPane = new JScrollPane(center);
        jScrollPane.setOpaque(false);
        jScrollPane.setBorder(null);
        jScrollPane.getViewport().setOpaque(false);

        add(jScrollPane, BorderLayout.CENTER);
    }

    private void initCenterUnit() {
        java.util.List<BaseUnitBean> units = unitService.getUnitsByUnitType(unit_type);
        JPanel centerTop = new JPanel();
        centerTop.setOpaque(false);
        centerTop.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel[] toplabels = new JLabel[7];
        for (int i = 0; i < toplabels.length; i++) {
            switch (i) {
                case 0:
                    toplabels[i] = new JLabel("设备地址", JLabel.CENTER);
                    break;
                case 1:
                    toplabels[i] = new JLabel("设备名称", JLabel.CENTER);
                    break;
                case 2:
                    toplabels[i] = new JLabel("时间", JLabel.CENTER);
                    break;
                case 3:
                    toplabels[i] = new JLabel("测量值", JLabel.CENTER);
                    break;
                case 4:
                    toplabels[i] = new JLabel("水位", JLabel.CENTER);
                    break;
                case 5:
                    toplabels[i] = new JLabel("链接状态", JLabel.CENTER);
                    break;
                case 6:
                    toplabels[i] = new JLabel("水尺常数", JLabel.CENTER);
                    break;
            }
            toplabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            toplabels[i].setPreferredSize(new Dimension(120, 20));
            centerTop.add(toplabels[i]);
        }
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH ;
        center.setLayout(gbl);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(centerTop, gbc);
        center.add(centerTop);
        panels = new Hashtable<>();
        for (BaseUnitBean unit : units) {
            WaterCollectStatesUnitPanel statesUnitPanel = new WaterCollectStatesUnitPanel((WaterUnitBean) unit);
            panels.put(unit.getUnit_num(), statesUnitPanel);
            gbc.gridy++;
            gbl.setConstraints(statesUnitPanel, gbc);
            center.add(statesUnitPanel.init());
        }
    }

    private void clear() {
        center.removeAll();
    }

    public void updateUnit() {
        clear();
        initCenterUnit();
        validate();
    }

    public void addStart(byte unit_type, final byte unit_num) {
        if (startFlags.containsKey(unit_num) && startFlags.get(unit_num) || startUnits.containsKey(unit_num)) {
            return;
        }
        final Timer startTimer = new Timer();
        startUnits.put(unit_num, startTimer);
        startTimes.put(unit_num, 1);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (startUnits.size() == 0 || startTimes.get(unit_num) >= 4) {
                    startTimes.put(unit_num, 1);
                    startUnits.remove(unit_num);
                    startTimer.cancel();
                }
                BaseS2G s2g = agreementConfig.getS2G("waterstartcollect");
                byte[] bytes = new byte[4];
                bytes[0] = 0;//网关类型
                bytes[1] = 1;//网关编号
                bytes[2] = 0;//单元类型
                bytes[3] = unit_num;//单元类型
                s2g.setDatas(bytes);
                s2g.resolve();
                connect.send(s2g.getResult());
                Integer times = startTimes.get(unit_num);
                startTimes.put(unit_num, times == null ? 1 : ++times);
            }
        };
        startTimer.schedule(task, 0, 3000);
    }

    public void removeStart(byte unit_type, byte unit_num) {
        startFlags.put(unit_num, true);
        if (startUnits.containsKey(unit_num)) {
            Timer timer = startUnits.remove(unit_num);
            timer.cancel();
        }
    }

    public void addData(WaterDataBean dataBean) {
        WaterCollectStatesUnitPanel statesUnitPanel = panels.get(dataBean.getUnit_num());
        if (statesUnitPanel != null) {
            statesUnitPanel.addData(dataBean);
        }
    }

    public void stopConnect(byte unit_type, byte unit_num) {
        WaterCollectStatesUnitPanel unitpanel = panels.get(unit_num);
        if (unitpanel != null) {
            unitpanel.stopConnect();
        }
    }

}
