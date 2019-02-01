package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import com.thingtek.beanServiceDao.proinfo.service.ProInfoService;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.WaterUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.com.entity.BaseS2G;
import com.thingtek.config.agreement.AgreementConfig;
import com.thingtek.serialPort.SerialConnect;
import com.thingtek.view.component.dialog.CollectPeriodDialog;
import com.thingtek.view.component.panel.CategoryPanel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.homePage.WaterCollectStatesPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.SystemPanel2WaterInitTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Timer;

/*
    top 区域 : 标题
    center 区 : 左右岸饼状图
    bottom 区 : 控制按钮
    {
        开始采集
        停止采集
        采集间隔
        数据查询
        保存路径
    }
 */

@Component
public class WaterDataCollectPanel extends BasePanel implements Collect {

    public WaterDataCollectPanel() {
        authority = UserService.UP;
    }

    private byte unit_type = 1;

    private long period = 5000; //毫秒

    public void setPeriod(long period) {
        this.period = period * 1000;
        periodShow.setText(String.valueOf(period) + "秒");
    }

    public long getPeriod() {
        return period / 1000;
    }

    private boolean collect;

    public boolean isCollect() {
        return collect;
    }

    @Resource
    private SerialConnect connect;
    @Resource
    private AgreementConfig agreementConfig;
    @Resource
    private UnitService unitService;
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private Shell shell;

    @Override
    public WaterDataCollectPanel init() {
        setOpaque(false);
        setLayout(new BorderLayout());
        initTop();
        initCenter();
        initBottom();
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        return this;
    }


    private JLabel top;

    private void initTop() {
        top = new JLabel("实时水位", JLabel.CENTER);
    }

    private JPanel center;

    public JPanel getCenter() {
        return center;
    }

    private CategoryPanel leftBank;
    private CategoryPanel rightBank;
    private CategoryPanel allBank;

    private CardLayout cardLayout;

    private Map<BaseUnitBean, Number> valueMap;

    private void initCenter() {
        initLeftBank();
        initRightBank();
        initAllBank();
        cardLayout = new CardLayout();
        center = new JPanel(cardLayout);
        JPanel left_right_Bank = new JPanel(new GridLayout(2, 1));
        left_right_Bank.add(leftBank);
        left_right_Bank.add(rightBank);
        center.add(left_right_Bank, "1");
        center.add(allBank, "2");
        center.setEnabled(false);

        java.util.List<BaseUnitBean> units = unitService.getUnitsByUnitType(unit_type);
        valueMap = new TreeMap<>(new Comparator<BaseUnitBean>() {
            @Override
            public int compare(BaseUnitBean o1, BaseUnitBean o2) {
                return o1.getUnit_num() - o2.getUnit_num();
            }
        });

        for (BaseUnitBean unit : units) {
            valueMap.put(unit, 0);
        }
        addData();
    }


    private void initAllBank() {
        allBank = new CategoryPanel();
        allBank.setValueAxisLabel("全岸");
        allBank.init();
        /*for (int i = 1; i <= 31; i++) {
            allBank.addData(i * 0.01, String.valueOf(i));
        }*/
    }

    private void initRightBank() {
        rightBank = new CategoryPanel();
        rightBank.setValueAxisLabel("右岸");
        rightBank.init();
       /* for (int i = 1; i <= 10; i++) {
            rightBank.addData(i * 0.01, String.valueOf(i));
        }*/

    }

    private void initLeftBank() {
        leftBank = new CategoryPanel();
        leftBank.setValueAxisLabel("左岸");
        leftBank.init();
        /*for (int i = 11; i <= 30; i++) {
            leftBank.addData(i * 0.01, String.valueOf(i));
        }*/
    }

    /*
     bottom 区 : 控制按钮
    {
        开始采集
        停止采集
        采集间隔
        数据查询
        保存路径
    }
     */
    private JPanel bottom;
    private JButton start;
    private JButton stop;
    private JButton periodSet;
    private JLabel periodShow;

    public void initBottom() {

        bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        start = new JButton("开始采集");
        start.setOpaque(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (systemPanel2WaterInitTest.isCollect()) {
                    errorMessage("正在进行水尺常数率定,请稍后再试");
                    return;
                }
                ProInfoBean proInfoBean = proInfoService.getInfo();
                if (proInfoBean == null || proInfoBean.getVertical_value() == null) {
                    errorMessage("尚未输入垂直比尺,请先输入垂直比尺");
                    return;
                }
                if (checkUnitInitValue()) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "部分设备水尺常数未设置,是否继续采集",
                            "提示",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }
                start.setEnabled(false);
                periodSet.setEnabled(false);
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        stop.setEnabled(true);
                    }
                };
                timer.schedule(task, 0);
                collect = true;
                testConnect();
//                collect();
            }
        });
        /*JButton start1 = new JButton("采集");
        start1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInit();
            }
        });*/

        stop = new JButton("停止采集");
        stop.setOpaque(false);
        stop.setEnabled(false);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop.setEnabled(false);
                collect = false;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        start.setEnabled(true);
                        periodSet.setEnabled(true);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 0);
                setStopCollect();

            }
        });

        periodSet = new JButton("采集周期");
        periodSet.setOpaque(false);
        periodSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CollectPeriodDialog dialog = new CollectPeriodDialog(shell, "采集周期", factorys.getIconFactory().getImage("set"));
                dialog.setFactorys(factorys);
                dialog.setCollect(WaterDataCollectPanel.this);
                dialog.initDialog().visible();
            }
        });
        periodShow = new JLabel(String.valueOf(period / 1000) + "秒");
        periodShow.setOpaque(false);

        JButton left$right = new JButton("左右岸显示");
        left$right.setOpaque(false);
        left$right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(center, "1");
            }
        });
        JButton all = new JButton("沿岸显示");
        all.setOpaque(false);
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(center, "2");
            }
        });


        bottom.add(start);
//        bottom.add(start1);
        bottom.add(stop);
        bottom.add(periodSet);
        bottom.add(periodShow);
        bottom.add(left$right);
        bottom.add(all);
    }

    private int connectTimes = 1;
    private int stopTimes = 1;
    private Timer connectTimer;
    private Timer collectTimer;
    private Timer stopTimer;
    private Hashtable<Byte, Timer> startUnits = new Hashtable<>();
    private Hashtable<Byte, Boolean> startFlags = new Hashtable<>();
    private Hashtable<Byte, Integer> startTimes = new Hashtable<>();
    private Vector<BaseUnitBean> stopUnits = new Vector<>();
    private Vector<Byte> stopFlags = new Vector<>();

    @Resource
    private WaterCollectStatesPanel statePanel;
    @Resource
    private SystemPanel2WaterInitTest systemPanel2WaterInitTest;

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
                    falseMessage("水位测量,连接超时,请检查设备连接!");
                    start.setEnabled(true);
                    stop.setEnabled(false);
                    periodSet.setEnabled(true);
                    connectTimes = 1;
                    collect = false;
                    if (connectTimer != null) {
                        connectTimer.cancel();
                        connectTimer = null;
                    }
                }
            }
        };
        connectTimer.schedule(task, 0, 1000);
    }

    public void stopTestConnect() {
        connectTimes = 1;
        if (connectTimer != null) {
            connectTimer.cancel();
            connectTimer = null;
        }
    }

    public void updateUnit() {
        leftBank.clearData();
        rightBank.clearData();
        allBank.clearData();
        Map<BaseUnitBean, Number> valuemaps = new TreeMap<>(new Comparator<BaseUnitBean>() {
            @Override
            public int compare(BaseUnitBean o1, BaseUnitBean o2) {
                return o1.getUnit_num() - o2.getUnit_num();
            }
        });
        for (BaseUnitBean unit : unitService.getUnitsByUnitType(unit_type)) {
            valuemaps.put(unit, valueMap.get(unit));
        }
        valueMap = valuemaps;
        addData();
    }


    public void addData(BaseDataBean dataBean) {
        Set<Map.Entry<BaseUnitBean, Number>> entries = valueMap.entrySet();
        for (Map.Entry<BaseUnitBean, Number> entry : entries) {
            BaseUnitBean unit = entry.getKey();
            if (Objects.equals(unit.getUnit_type(), dataBean.getUnit_type()) && Objects.equals(unit.getUnit_num(), dataBean.getUnit_num())) {
                entry.setValue(dataBean.getValue1());
                switch (unit.getPoint_num()) {
                    case 1:
                        leftBank.addData(dataBean.getValue1(), unit.getUnit_name());
                        break;
                    case 2:
                        rightBank.addData(dataBean.getValue1(), unit.getUnit_name());
                        break;
                    default:
                }
                allBank.addData(dataBean.getValue1(), unit.getUnit_name());
                return;
            }
        }
    }

    private void addData() {
        Set<Map.Entry<BaseUnitBean, Number>> entries = valueMap.entrySet();
        for (Map.Entry<BaseUnitBean, Number> entry : entries) {
            BaseUnitBean unit = entry.getKey();
            switch (unit.getPoint_num()) {
                case 1:
                    leftBank.addData(entry.getValue(), unit.getUnit_name());
                    break;
                case 2:
                    rightBank.addData(entry.getValue(), unit.getUnit_name());
                    break;
                default:
            }
            allBank.addData(entry.getValue(), unit.getUnit_name());
        }
    }

    public void collect() {
        statePanel.stopCollect();//停止主页采集
        collectTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                BaseS2G s2g = agreementConfig.getS2G("wateruploaddata");
                int index = 0;
                List<BaseUnitBean> untis = unitService.getUnitsByUnitType(unit_type);
                byte[] datas = new byte[untis.size() * 2 + 2];
                datas[index++] = 0;
                datas[index++] = 1;//单一网关
                for (BaseUnitBean unit : untis) {
                    datas[index++] = 0;
                    datas[index++] = unit.getUnit_num();
                }
                s2g.setDatas(datas);
                s2g.resolve();
                connect.send(s2g.getResult());
            }
        };
        collectTimer.schedule(task, 0, period);
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
                    startTimes.remove(unit_num);
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
                if (times == null) {
                    times = 1;
                }
                times++;
                startTimes.put(unit_num, times);
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

    @Override
    public void stopCollect() {
        connectTimes = 1;
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
        stopFlags.clear();
        stopUnits.clear();
    }

    private void setStopCollect() {
        stopCollect();
        stopTimes = 1;
        if (stopTimer != null) {
            stopTimer.cancel();
            stopTimer = null;
        }
        stopUnits.addAll(unitService.getUnitsByUnitType(unit_type));
        stopTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (stopUnits.size() == 0 || stopTimes >= 4) {
                    stopTimes = 1;
                    for (BaseUnitBean unitBean : stopUnits) {
                        statePanel.stopConnect(unitBean.getUnit_type(), unitBean.getUnit_num());
                    }
                    stopUnits.clear();
                    if (stopTimer != null) {
                        stopTimer.cancel();
                        stopTimer = null;
                    }
                }
                Iterator<BaseUnitBean> iterator = stopUnits.iterator();
                while (iterator.hasNext()) {
                    BaseUnitBean unit = iterator.next();
                    if (stopFlags.contains(unit.getUnit_num())) {
                        continue;
                    }
                    BaseS2G s2g = agreementConfig.getS2G("waterstopcollect");
                    byte[] bytes = new byte[4];
                    bytes[0] = 0;//网关类型
                    bytes[1] = 1;//网关编号
                    bytes[2] = 0;//单元类型
                    bytes[3] = unit.getUnit_num();//单元类型
                    s2g.setDatas(bytes);
                    s2g.resolve();
                    connect.send(s2g.getResult());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                stopTimes++;
            }
        };
        stopTimer.schedule(task, 0, stopUnits.size() * 100 + 3000);
    }

    public void addStop(byte unit_type, byte unit_num) {
        for (BaseUnitBean stopUnit : stopUnits) {
            if (Objects.equals(stopUnit.getUnit_num(), unit_num)) {
//                iterator.remove();
                stopFlags.add(unit_num);
                statePanel.stopConnect(unit_type, unit_num);
            }
        }
    }

    public boolean checkUnitInitValue() {
        //true 有null
        //false 没有null
        List<Boolean> flags = new ArrayList<>();
        for (BaseUnitBean unitBean : unitService.getUnitsByUnitType(unit_type)) {
            WaterUnitBean unit = (WaterUnitBean) unitBean;
            flags.add(unit.getValue_init() == null);
        }
        return flags.contains(true);
    }
}
