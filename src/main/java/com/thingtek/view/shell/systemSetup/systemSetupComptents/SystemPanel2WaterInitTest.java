package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.data.entity.WaterDataBean;
import com.thingtek.beanServiceDao.net.service.NetService;
import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import com.thingtek.beanServiceDao.proinfo.service.ProInfoService;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.WaterUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.com.entity.BaseS2G;
import com.thingtek.config.agreement.AgreementConfig;
import com.thingtek.serialPort.SerialConnect;
import com.thingtek.util.ExcelUtil;
import com.thingtek.util.Util;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.WaterUnitTestTableModel;
import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

/*
   水尺常数率定 页面
 */
@Component
public class SystemPanel2WaterInitTest extends BaseSystemPanel {

    public SystemPanel2WaterInitTest() {
        authority = UserService.UP;
    }

    @Resource
    private SerialConnect connect;

    private boolean collect;

    public boolean isCollect() {
        return collect;
    }

    private JTable table;
    private JTextField jtfFirstTest, jtfSecondTest, jtfThirdTest;

    @Resource
    private AgreementConfig agreementConfig;

    private byte net_type = 0;//固定网关类型
    private byte unit_type = 1;//固定单元类型
    @Resource
    private WaterUnitTestTableModel model;

    @Resource
    private UnitService unitService;

    @Resource
    private NetService netService;

    @Resource
    private ProInfoService proInfoService;

    private ProInfoBean proInfoBean;

    @Override
    protected void initCenter() {
        super.initCenter();
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        JPanel title = new JPanel();
        title.setOpaque(false);
        center.add(title, BorderLayout.NORTH);

        Dimension dimension = new Dimension(50, 20);
        title.add(new JLabel("静水面高程"));
        title.add(new JLabel("一次测量:"));
        jtfFirstTest = new JTextField();
        jtfFirstTest.setPreferredSize(dimension);

        title.add(jtfFirstTest);
        title.add(new JLabel("米"));
        title.add(new JLabel("二次测量:"));
        jtfSecondTest = new JTextField();
        jtfSecondTest.setPreferredSize(dimension);
        title.add(jtfSecondTest);
        title.add(new JLabel("米"));
        title.add(new JLabel("三次测量:"));
        jtfThirdTest = new JTextField();
        jtfThirdTest.setPreferredSize(dimension);

        title.add(jtfThirdTest);
        title.add(new JLabel("米"));

        proInfoBean = proInfoService.getInfo();
        if (proInfoBean != null) {
            if (proInfoBean.getFirst_value() != null) {
                jtfFirstTest.setText(String.valueOf(proInfoBean.getFirst_value()));
            }
            if (proInfoBean.getSecond_value() != null) {
                jtfSecondTest.setText(String.valueOf(proInfoBean.getSecond_value()));
            }
            if (proInfoBean.getThird_value() != null) {
                jtfThirdTest.setText(String.valueOf(proInfoBean.getThird_value()));
            }
        }

        table = new JTable(model);
        table.setOpaque(false);
        initializeTable();
        initTableData();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        center.add(scrollPane);
        addCenter(center);
    }

    @Resource(name = "waterUnitInitTestTableRanderer")
    private TCR tcr;
    // 初始化表格

    private void initializeTable() {
        tcr.setCollect_name("率定");
        table.setDefaultRenderer(String.class, tcr);
        table.setDefaultRenderer(Number.class, tcr);
        table.setDefaultRenderer(Integer.class, tcr);
        table.setDefaultRenderer(Float.class, tcr);
        table.setDefaultRenderer(Double.class, tcr);
        table.setDefaultRenderer(Date.class, tcr);
        table.setDefaultRenderer(Object.class, tcr);
        // 表头设置
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);// 表头居中

        Dimension dimension = dtcr.getSize();
        dimension.height = Util.TableHeadHeight;
        dtcr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(dtcr);
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽不可修改
        tableHeader.setResizingAllowed(false);
        // 自动排序
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(Util.TableRowHeight);// 设置行高
    }

    private EditButton ebFirstTest;
    private EditButton ebSecondTest;
    private EditButton ebThirdTest;
    private EditButton save;
    private EditButton export;

    @Resource
    private WaterDataCollectPanel waterDataCollectPanel;

    @Resource
    private SystemPanel2WaterInfo systemPanel2WaterInfo;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ebFirstTest = addTool("一次率定", "edit");
        ebFirstTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String first = jtfFirstTest.getText();
                if (!isNum(first)) {
                    errorMessage("\"一次测量值\"输入有误请重新输入");
                    return;
                }
                test(1, Double.parseDouble(first));
            }
        });
        ebSecondTest = addTool("二次率定", "edit");
        ebSecondTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String first = jtfSecondTest.getText();
                if (!isNum(first)) {
                    errorMessage("\"二次测量值\"输入有误请重新输入");
                    return;
                }
                test(2, Double.parseDouble(first));
            }
        });
        ebThirdTest = addTool("三次率定", "edit");
        ebThirdTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String first = jtfThirdTest.getText();
                if (!isNum(first)) {
                    errorMessage("\"三次测量值\"输入有误请重新输入");
                    return;
                }
                test(3, Double.parseDouble(first));
            }
        });
        save = addTool("保存", "apply");
        save.setToolTipText("保存平均值为水尺常数");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<BaseUnitBean> unitList = unitService.getUnitsByUnitType(unit_type);

                int result = 0;

                switch (proInfoBean.getTest_count()) {
                    case 0: //未率定
                        falseMessage("未进行率定无法导入!");
                        return;
                    case 1: //第一次率定
                    case 2: //第二次率定
                        result = JOptionPane.showConfirmDialog(null, "只进行" + proInfoBean.getTest_count() + "次率定 确定导入?", "提示", JOptionPane.YES_NO_OPTION);
                        break;
                    case 3: //第三次率定
                        result = JOptionPane.showConfirmDialog(null, "确定导入?", "提示", JOptionPane.YES_NO_OPTION);
                        break;
                }
                if (result == JOptionPane.OK_OPTION) {
                    for (BaseUnitBean baseUnitBean : unitList) {
                        WaterUnitBean unit = (WaterUnitBean) baseUnitBean;
                        unit.setValue_init(unit.getAveInitValue());
                        if (unitService.updateAll(unit_type)) {
                            successMessage("保存成功");
                            systemPanel2WaterInfo.update();
                        } else {
                            falseMessage("保存失败");
                        }
                    }
                }
            }
        });
        export = addTool("导出", "database_download");
        export.setToolTipText("下载到Excel");
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() == 0) {
                    errorMessage("当前表中无数据，或数据已清空");
                } else {
                    try {
                        ExcelUtil.Export2Excel(table, "数据");
                    } catch (IOException e1) {
                        falseMessage("导出失败,请稍后重试");
                    }
                }
            }
        });
    }


    private int times = 1;
    private Timer connectTimer;//测试连接计时器
    private Timer collectTimer;//获取数据计时器
    private Timer stopTimer;//停止采集计时器
    //测试单元集合
    private java.util.List<BaseUnitBean> testInitUnits;
    private Vector<BaseUnitBean> collectUnits = new Vector<>();//获取数据
    private Vector<BaseUnitBean> stopUnits = new Vector<>();//停止采集

    private Hashtable<Byte, Timer> startUnits = new Hashtable<>();
    private Hashtable<Byte, Boolean> startFlags = new Hashtable<>();
    private Hashtable<Byte, Integer> startTimes = new Hashtable<>();
    private Vector<Byte> stopFlags = new Vector<>();

    //检测
    private void test(int count, double value) {
        if (waterDataCollectPanel.isCollect()) {
            errorMessage("正在采集数据!请先停止采集");
            return;
        }
        proInfoBean = proInfoService.getInfo();
        if (proInfoBean == null || proInfoBean.getVertical_value() == null) {
            errorMessage("尚未输入垂直比尺,请先输入垂直比尺");
            return;
        }
        switch (count) {
            case 1:
                proInfoBean.setFirst_value(value);
                break;
            case 2:
                proInfoBean.setSecond_value(value);
                break;
            case 3:
                proInfoBean.setThird_value(value);
                break;
        }
        int result = 0;
        if (count < proInfoBean.getTest_count() + 1) { //确定重新 ?
            result = JOptionPane.showConfirmDialog(null, "确定重新第" + count + "次率定?", "提示", JOptionPane.YES_NO_OPTION);
        } else if (count > proInfoBean.getTest_count() + 1) { //请先上一次
            falseMessage("请先进行第" + (proInfoBean.getTest_count() + 1) + "次率定");
            return;
        } else { //正常进行
            proInfoBean.setTest_count(count);
        }
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        proInfoService.save(proInfoBean);
        testInitUnits = unitService.getUnitsByUnitType(unit_type);
        setEnable(false);
        testConnect();
    }

    //测试连接
    private void testConnect() {
        collect = true;
        connectTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                BaseS2G s2g = agreementConfig.getS2G("waternetconnect");
                byte[] data = new byte[]{0, 1};//单一网关
                s2g.setDatas(data);
                s2g.resolve();
                connect.send(s2g.getResult());
                times++;
                if (times >= 4) {
                    falseMessage("连接超时,请检查设备连接!");
                    setEnable(true);
                    times = 1;
                    if (connectTimer != null) {
                        connectTimer.cancel();
                        connectTimer = null;
                    }
                    collect = false;
                }
            }
        };
        connectTimer.schedule(task, 0, 1000);
    }


    private WaterUnitBean getTestInitUnit(WaterDataBean data) {
        for (BaseUnitBean unit : testInitUnits) {
            if (Objects.equals(unit.getUnit_type(), data.getUnit_type()) && Objects.equals(unit.getUnit_num(), data.getUnit_num())) {
                return (WaterUnitBean) unit;
            }
        }
        return null;
    }

    private void setEnable(boolean flag) {
        ebFirstTest.setEnabled(flag);
        ebSecondTest.setEnabled(flag);
        ebThirdTest.setEnabled(flag);
        save.setEnabled(flag);
        export.setEnabled(flag);
    }

    private void saveInit(WaterDataBean data) {
        WaterUnitBean waterunit = getTestInitUnit(data);
        if (waterunit != null) {
            float init = initOpration(data.getValue1(), proInfoBean.getTest_count());
            switch (proInfoBean.getTest_count()) {
                case 1:
                    waterunit.setFirst_value(init);
                    break;
                case 2:
                    waterunit.setSecond_value(init);
                    break;
                case 3:
                    waterunit.setThird_value(init);
                    break;
            }
        }
        unitService.updateAll(unit_type);
        updateUnit();
    }

    public void updateUnit() {
        model.clearData();
        initTableData();
    }

    /*
        常数计算
        @para testvalue 测量值
        @para count 率定次数
     */
    private float initOpration(float testvalue, int count) {
        double userinput = 0;
        switch (count) {
            case 1:
                userinput = proInfoBean.getFirst_value();
                break;
            case 2:
                userinput = proInfoBean.getSecond_value();
                break;
            case 3:
                userinput = proInfoBean.getThird_value();
                break;
        }
        return (float) (userinput + testvalue * proInfoBean.getVertical_value() / 1000.0f);
    }

    private void initTableData() {
        java.util.List<Vector<Object>> list = new ArrayList<>();
        java.util.List<BaseUnitBean> units = unitService.getUnitsByUnitType(unit_type);
        for (BaseUnitBean unit : units) {
            list.add(unit.getTestTableCol());
        }
        model.addDatas(list);
    }

    public void addStart(byte unit_type, final byte unit_num) {
        if (startFlags.containsKey(unit_num) && startFlags.get(unit_num)
                || startUnits.containsKey(unit_num)
                ) {
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

    public void stopTestConnect() {
        times = 1;
        if (connectTimer != null) {
            connectTimer.cancel();
            connectTimer = null;
        }
    }

    public void collect() {
        collectUnits.clear();
        collectUnits.addAll(testInitUnits);
        collectTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (collectUnits.size() == 0 || times >= 10) {
                    times = 1;
                    collectUnits.clear();
                    if (collectTimer != null) {
                        collectTimer.cancel();
                        collectTimer = null;
                    }
                    setStop();
                    return;
                }
                BaseS2G s2g = agreementConfig.getS2G("wateruploaddata");
                int index = 0;
                byte[] datas = new byte[collectUnits.size() * 2 + 2];
                datas[index++] = 0;
                datas[index++] = 1;//单一网关
                for (BaseUnitBean unit : collectUnits) {
                    datas[index++] = unit.getUnit_type();
                    datas[index++] = unit.getUnit_num();
                }
                s2g.setDatas(datas);
                s2g.resolve();
                connect.send(s2g.getResult());
                times++;
            }
        };
        collectTimer.schedule(task, 0, collectUnits.size() * 100 + 30000);
    }

    public void addCollect(WaterDataBean data) {
        saveInit(data);
        Iterator<BaseUnitBean> iterator = collectUnits.iterator();
        while (iterator.hasNext()) {
            BaseUnitBean unit = iterator.next();
            if (Objects.equals(unit.getUnit_num(), data.getUnit_num())) {
                iterator.remove();
                return;
            }
        }
    }

    public void stopCollect() {
        times = 1;
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
        stopUnits.clear();
        stopFlags.clear();
    }

    private void setStop() {
        stopCollect();
        stopUnits.addAll(testInitUnits);
        if (stopTimer != null) {
            stopTimer.cancel();
            stopTimer = null;
        }
        stopTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (stopUnits.size() == 0 || times >= 4) {
                    times = 1;
                    stopUnits.clear();
                    if (stopTimer != null) {
                        stopTimer.cancel();
                        stopTimer = null;
                    }
                    collect = false;
                    setEnable(true);
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
                        if (!iterator.hasNext()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                times++;
            }
        };
        stopTimer.schedule(task, 0, stopUnits.size() * 100 + 3000);
    }

    public void addStop(byte unit_type, byte unit_num) {
        for (BaseUnitBean unit : stopUnits) {
            if (Objects.equals(unit.getUnit_num(), unit_num)) {
                stopFlags.add(unit_num);
            }
        }
    }

}