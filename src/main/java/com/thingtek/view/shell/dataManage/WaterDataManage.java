package com.thingtek.view.shell.dataManage;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.com.data.entity.DataSearchPara;
import com.thingtek.util.ExcelUtil;
import com.thingtek.util.Util;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.panel.LinePanel;
import com.thingtek.view.component.panel.ToolPanel;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.BaseTableModel;
import com.thingtek.view.shell.BasePanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.*;

@Component
public class WaterDataManage extends BasePanel {


    private JComboBox jcbCollectNames;
    private JComboBox<String> jcbUnitNames;
    private Check2SPinner c2s1, c2s2;
    private JTable table;

    private WaterDataManage() {
        authority = UserService.UP;
    }


    private BaseTableModel tableModel;

    public WaterDataManage init() {
        this.setLayout(new BorderLayout());

        // 加载工具栏
        this.initToolPane();

        this.initCenter();
        this.initChart();
        this.initTable();
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setLeftComponent(tablePanel);
        jSplitPane.setRightComponent(chartPanel);
        jSplitPane.setOpaque(false);
        jSplitPane.setDividerLocation(500);
        centerPanel.add(jSplitPane, "table");
        this.add(centerPanel, BorderLayout.CENTER);
        this.initWait();

        // 加载表格
        return this;
    }

    @Override
    public void manager(UserBean user) {
        super.manager(user);
    }

    private JPanel centerPanel;
    private JPanel tablePanel;
    private CardLayout card;

    private void initCenter() {
        centerPanel = new JPanel();
        card = new CardLayout();
        centerPanel.setLayout(card);
        centerPanel.add(new JPanel(), "null");
//        this.add(centerPanel, BorderLayout.CENTER);
    }

    private LinePanel chartPanel;

    private void initChart() {
        chartPanel = new LinePanel();

    }

    private void initWait() {
        JPanel jPanel = new JPanel(new BorderLayout());
        JLabel waitlabel = new JLabel(factorys.getIconFactory().getIcon("progress"));
        jPanel.add(waitlabel, BorderLayout.CENTER);
        JLabel textlabel = new JLabel("正在加载数据请稍后", JLabel.CENTER);
        jPanel.add(textlabel, BorderLayout.SOUTH);
        centerPanel.add(jPanel, "wait");
    }

    public void manager() {
        toolBarR.remove(delete);
        toolBarR.remove(clear);
        toolBarR.validate();
    }

    private JPanel toolBarR;
    private JButton delete;
    private JButton clear;
    private DataSearchPara para;

    /**
     * 初始化顶部工具栏面板 jToolPane：顶部工具栏面板有jtb1、jtb2左右两个工具栏，分别加载左边和右边的工具按钮
     */
    private void initToolPane() {

        /*
         * 工具栏面板
         */
        JPanel toolPane = new ToolPanel(new BorderLayout());
        toolPane.setPreferredSize(new Dimension(toolPane.getWidth(), 40));
        this.add(toolPane, BorderLayout.NORTH);

        /*
         * 工具栏左半边
         */
        JPanel toolBarL = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolBarL.setOpaque(false);
        toolPane.add(toolBarL, BorderLayout.WEST);

        toolBarL.add(Box.createHorizontalStrut(5));

        // 设置的文本设置为html代码即可实现JLabel多行显示
        JTextArea jta1 = new JTextArea("采集\n类型");
        jta1.setOpaque(false);
        jta1.setEditable(false);
        toolBarL.add(jta1);

        // 仪器类型
        jcbCollectNames = new JComboBox<>(unitService.getCollectNames());
        jcbCollectNames.setSelectedItem(null);
        jcbCollectNames.setMaximumSize(new Dimension(80, 20));
        jcbCollectNames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUnitName();
            }
        });
        toolBarL.add(jcbCollectNames);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta3 = new JTextArea("设备\n名称");
        jta3.setOpaque(false);
        jta3.setEditable(false);
        toolBarL.add(jta3);

        jcbUnitNames = new JComboBox<>();
        jcbUnitNames.setMaximumSize(new Dimension(100, 20));
        toolBarL.add(jcbUnitNames);
        JButton refresh = new JButton("刷新", factorys.getIconFactory().getIcon("refresh"));
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshUnitName();
            }
        });
        toolBarL.add(refresh);
        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta4 = new JTextArea("起始\n时间");
        jta4.setOpaque(false);
        jta4.setEditable(false);
        toolBarL.add(jta4);

        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        Date date2 = ca.getTime();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date date = ca.getTime();
        // 起始时间
        c2s1 = new Check2SPinner(false, date);
        toolBarL.add(c2s1);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta5 = new JTextArea("终止\n时间");
        jta5.setOpaque(false);
        jta5.setEditable(false);
        toolBarL.add(jta5);

        // 终止时间
        c2s2 = new Check2SPinner(false, date2);
        toolBarL.add(c2s2);

        toolBarL.add(Box.createHorizontalStrut(5));
        search = new JButton("查询", factorys.getIconFactory().getIcon("search"));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("getsearchcondition0:"+new Date());
                if (jcbCollectNames.getSelectedIndex() < 0) {
                    errorMessage("请先选择设备类型");
                    return;
                }
                if (jcbCollectNames.getSelectedItem() == null) {
                    errorMessage("请先选择采集类型");
                    return;
                }
//                tableModel.clearData();
//                chartPanel.clear();
//                System.out.println("getsearchcondition1:"+new Date());
                getSearchConditon();
//                System.out.println("getsearchcondition2:"+new Date());
                search.setEnabled(false);
                card.show(centerPanel, "wait");
                toolBarR.setVisible(false);
                para.setStartcount(0);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                searchDataThread();
//                    }
//                }).start();

            }
        });
        toolBarL.add(search);

        /*
         * 工具栏右半边
         */
        toolBarR = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolBarR.setOpaque(false);
        toolPane.add(toolBarR, BorderLayout.EAST);

        Dimension buttonSize = new Dimension(38, 38);

        delete = new JButton(factorys.getIconFactory().getIcon("delete"));
        delete.setBorder(factorys.getBorderFactory().getEmptyBorder());
        delete.setToolTipText("删除选中的数据");
        delete.setPreferredSize(buttonSize);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 得到鼠标选中行
                int[] selRows = table.getSelectedRows();
                // 做个判断，如果没有选中行，则弹出提示
                if (selRows.length <= 0) {
                    errorMessage("请选中要删除的数据行");
                    return;
                }
                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要删除选中数据？",
                        "删除", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION)
                    return;

                Map<BaseUnitBean, java.util.List<Date>> dates = getSelectTable();
                if (dataService.deleteData(dates)) {
                    successMessage("数据已成功删除");
                    int[] rows = new int[selRows.length];
                    int j = 0;
                    for (int i : selRows) {
                        rows[j] = table.convertRowIndexToModel(i);
                        j++;
                    }
                    Arrays.sort(rows);// rows升序排序
                    tableModel.removeRows(rows);
                    chartPanel.delete(dates);
                } else {
                    falseMessage("数据删除失败,请稍后重试");
                }

            }
        });
        toolBarR.add(delete);

        clear = new JButton(factorys.getIconFactory().getIcon("clear"));
        clear.setBorder(factorys.getBorderFactory().getEmptyBorder());
        clear.setToolTipText("清空表中数据");
        clear.setPreferredSize(buttonSize);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 做个判断，如果没有选中行，则弹出提示
                int rowCount = table.getRowCount();
                if (rowCount == 0) {
                    errorMessage("当前表中无数据，或数据已清空");
                    return;
                }
                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要清空表中数据？",
                        "清空", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION)
                    return;
                if (dataService.deleteData(getAllTable())) {
                    successMessage("数据已成功清空");
                    BaseTableModel model = (BaseTableModel) table.getModel();
                    model.clearData();
                    chartPanel.clear();
                } else {
                    falseMessage("数据清空失败,请稍后重试");
                }

            }
        });
        toolBarR.add(clear);

        JButton export = new JButton(factorys.getIconFactory().getIcon("database_download"));
        export.setBorder(factorys.getBorderFactory().getEmptyBorder());
        export.setToolTipText("将表中数据下载到Excel");
        export.setPreferredSize(buttonSize);
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
        toolBarR.add(export);

        JButton print = new JButton(factorys.getIconFactory().getIcon("printer"));
        print.setBorder(factorys.getBorderFactory().getEmptyBorder());
        print.setToolTipText("打印表格");
        print.setPreferredSize(buttonSize);
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() == 0) {
                    errorMessage("当前表中无数据，或数据已清空");
                } else {
                    try {
                        table.print();
                    } catch (PrinterException e) {
                        falseMessage("打印失败,请稍后重试");
                    }
                }
            }
        });
        toolBarR.add(print);
    }

    private JButton search;

    private Map<BaseUnitBean, java.util.List<Date>> getSelectTable() {
        int[] selRows = table.getSelectedRows();
        Map<BaseUnitBean, java.util.List<Date>> map = new HashMap<>();
        for (int selRow : selRows) {
            getSelectData(map, selRow);
        }
        return map;
    }

    private Map<BaseUnitBean, java.util.List<Date>> getAllTable() {
        int selRows = table.getRowCount();
        Map<BaseUnitBean, java.util.List<Date>> map = new HashMap<>();
        for (int i = 0; i < selRows; i++) {
            getSelectData(map, i);
        }
        return map;
    }

    //根据行号获取数据
    private void getSelectData(Map<BaseUnitBean, java.util.List<Date>> map, int rowNum) {
        String collect_name = (String) table.getValueAt(rowNum, 0);
        String unit_name = (String) table.getValueAt(rowNum, 1);
        BaseUnitBean unitBean = unitService.getUnitByUnitName(collect_name, unit_name);
        Date date = (Date) table.getValueAt(rowNum, table.getColumnCount() - 1);
        if (map.containsKey(unitBean)) {
            map.get(unitBean).add(date);
        } else {
            java.util.List<Date> dates = new ArrayList<>();
            dates.add(date);
            map.put(unitBean, dates);
        }
    }


    private void searchDataThread() {
        search.setEnabled(false);
        card.show(centerPanel, "wait");
        toolBarR.setVisible(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                System.out.println("search1"+new Date());
                searchData();
                card.show(centerPanel, "table");
                search.setEnabled(true);
                toolBarR.setVisible(true);
//                System.out.println("search2"+new Date());
            }
        }).start();
    }

    private void searchData() {
//        System.out.println("search-1:"+new Date());
        System.gc();
//        System.out.println("search0:"+new Date());
        java.util.List<BaseDataBean> datas = dataService.getDataByPara(para);
        java.util.List<Vector<Object>> tableDatas = new ArrayList<>();
        Map<BaseUnitBean, java.util.List<BaseDataBean>> chartDatas = new TreeMap<>(new Comparator<BaseUnitBean>() {
            @Override
            public int compare(BaseUnitBean o1, BaseUnitBean o2) {
                return o1.getUnit_num() - o2.getUnit_num();
            }
        });
        for (BaseDataBean data : datas) {
            tableDatas.add(data.tableData());
            BaseUnitBean unit = unitService.getUnitBeanByUnittypeAndUnitnum(data.getUnit_type(), data.getUnit_num());
            if (chartDatas.containsKey(unit)) {
                chartDatas.get(unit).add(data);
            } else {
                java.util.List<BaseDataBean> chartdata = new ArrayList<>();
                chartdata.add(data);
                chartDatas.put(unit, chartdata);
            }

        }
//        System.out.println("search1:"+new Date());
        tableModel.addDatas(tableDatas);

        chartPanel.addData(chartDatas);

        card.show(centerPanel, "null");
    }


    /**
     * 初始化中间表格部分
     */
    private void initTable() {
        // 中部JTable
        tablePanel = new JPanel(new BorderLayout());
        table = new JTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Map<BaseUnitBean, java.util.List<Date>> map = getSelectTable();
                Set<BaseUnitBean> set = map.keySet();
                chartPanel.setLight(set);
            }
        });
        // 将JTable添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(table);
        /*final JScrollBar vbar = scrollPane.getVerticalScrollBar();
        vbar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) { //下拉加载
                if (vbar.getMaximum() - vbar.getVisibleAmount() <= vbar.getValue() + 1) {
                    load();
                }
            }
        });
        scrollPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }
        });*/
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        initializeTable();// 初始化表格
    }

    //上拉加载
    private void load() {
        if (para == null || para.getStartcount() == table.getRowCount()) {
            return;
        }
        para.setStartcount(table.getRowCount());
        searchDataThread();
    }

    @Resource(name = "waterDataTableRanderer")
    private TCR tcr;

    // 初始化表格
    private void initializeTable() {
        table.setDefaultRenderer(String.class, tcr);
        table.setDefaultRenderer(Number.class, tcr);
        table.setDefaultRenderer(Float.class, tcr);
        table.setDefaultRenderer(Integer.class, tcr);
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

    /**
     * 获取查询条件
     */
    private void getSearchConditon() {
        // 物理量
        this.para = new DataSearchPara();
        para.setUser(userService.getUser());
        String collect_name = (String) jcbCollectNames.getSelectedItem();
        String unit_name = jcbUnitNames.getSelectedIndex() > 0 ? (String) jcbUnitNames.getSelectedItem() : null;
        BaseUnitBean unit = unitService.getUnitByUnitName(collect_name, unit_name);
        byte unit_type = unitService.getCollectNumByCollectName(collect_name);
        para.setUnit_type(unit_type);
        para.setUnit_num(unit == null ? null : unit.getUnit_num());
        para.setT1(c2s1.getTime());
        Date t2 = c2s2.getTime();
        if (t2 != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(t2);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            para.setT2(c.getTime());
        }
        tcr.setCollect_name(collect_name);
        tableModel = tableConfig.getModel(collect_name);
        tableModel.clearData();
        table.setModel(tableModel);

    }

    private void refreshUnitName() {
        String type_name = (String) jcbCollectNames.getSelectedItem();
        if (type_name == null || type_name.equals("")) {
            errorMessage("请先选择采集类型");
            return;
        }
        Vector<String> sjbhs = unitService.getUnitNamesByCollectName(type_name);
        jcbUnitNames.removeAllItems();
        if (sjbhs.size() > 0) {
            jcbUnitNames.addItem("全部");
            for (String sjbh : sjbhs) {
                jcbUnitNames.addItem(sjbh);
            }
        }
    }

}

