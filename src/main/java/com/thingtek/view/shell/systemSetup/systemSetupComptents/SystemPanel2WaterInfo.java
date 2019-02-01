package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.serialPort.SerialTool;
import com.thingtek.util.Util;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.dialog.UnitDialog;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.WaterUnitTableModel;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.homePage.WaterCollectStatesPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

@Component
public class SystemPanel2WaterInfo extends BaseSystemPanel {

    private byte unit_type = 1;

    @Resource
    private UnitService unitService;
    @Resource
    private PointService pointService;

    @Resource
    private WaterUnitTableModel model;

    @Resource
    private Shell shell;

    private JTable table;

    @Override
    protected void initCenter() {
        super.initCenter();
        table = new JTable(model);
        table.setOpaque(false);

        this.initializeTable();

        initTableData();

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(i);
            JComboBox<String> comboBox;
            JTextField textField;
            switch (i) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    comboBox = new JComboBox<>(unitService.getSensers());
                    tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
                    break;
                case 4:
                case 7:
                    textField = new JTextField();
                    textField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char keychar = e.getKeyChar();
                            if (keychar < '0' || keychar > '9') {
                                JTextField jtf = (JTextField) e.getSource();
                                if (keychar != '.') {
                                    e.consume();
                                } else {
                                    if (jtf.getText().contains(".")) {
                                        e.consume();
                                    }
                                }
                            }
                        }

                    });
                    tableColumn.setCellEditor(new DefaultCellEditor(textField));
                    break;
                case 5:
                    comboBox = new JComboBox<>(pointService.getPointNames());
                    tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
                    break;
                case 6:
                    break;
                case 8:
                    break;
                case 9:
                    break;
            }

        }


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        addCenter(scrollPane);
    }

    @Resource
    private WaterDataCollectPanel waterDataCollect;
    @Resource
    private SystemPanel2WaterInitTest systemPanel2WaterInitTest;
    @Resource
    private WaterCollectStatesPanel waterCollectStatesPanel;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        EditButton add = addTool("添加", "add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnitDialog dialog = new UnitDialog(shell, "添加单元", factorys.getIconFactory().getImage("set"));
                dialog.setUnittype(unit_type);
                dialog.setFactorys(factorys);
                dialog.setUnitService(unitService);
                dialog.setPointService(pointService);
                dialog.initDialog().visible();
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        update();
                    }
                });
            }
        });
        EditButton save = addTool("保存", "apply");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waterDataCollect.isCollect()) {
                    falseMessage("水位正在采集,请停止采集后再进行修改");
                    return;
                }
                java.util.List<String> unit_names = new ArrayList<>();
                for (int j = 0; j < table.getRowCount(); j++) {
                    String name = (String) table.getValueAt(j, 2);
                    if (unit_names.contains(name)) {
                        errorMessage("设备名称重复请重新输入");
                        return;
                    }
                    unit_names.add(name);
                    String num = String.valueOf(table.getValueAt(j, 4));
                    if (!isNum(num)) {
                        errorMessage("水尺常数输入有误");
                        return;
                    }
                    num = String.valueOf(table.getValueAt(j, 7));
                    if (!isNum(num)) {
                        errorMessage("探针频率输入有误");
                        return;
                    } else {
                        float numf = Float.parseFloat(num);
                        if (numf <= 0 || numf > 10) {
                            errorMessage("探针频率输入有误");
                            return;
                        }
                    }
                }
                for (int i = 0; i < table.getRowCount(); i++) {
                    /*
                    1 单元编号
                    2 单元名称
                    3 单元类型
                    4 水尺常数
                    5 位置
                    6 位置索引
                    7 串口名称
                     */
                    BaseUnitBean unit = unitService.getUnitBeanByUnittypeAndUnitnum(unit_type, (Byte) table.getValueAt(i, 1));
                    if (unit == null) {
                        continue;
                    }
                    unit.resolveTable(table, i);
                    Integer point_num = pointService.getPointNumByName(unit.getPoint_name());
                    unit.setPoint_num(point_num != null ? point_num : 1);
                }
                if (unitService.updateAll(unit_type)) {
                    successMessage("保存成功");
                    update();
                } else {
                    falseMessage("保存失败");
                }

            }
        });
        EditButton delete = addTool("删除", "delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waterDataCollect.isCollect()) {
                    falseMessage("水位正在采集,请停止采集后再进行修改");
                    return;
                }
                java.util.List<BaseUnitBean> units = new ArrayList<>();
                for (int i = 0; i < table.getRowCount(); i++) {
                    /*
                    1 单元编号
                    2 单元名称
                    3 单元类型
                    4 水尺常数
                    5 位置
                    6 位置索引
                    7 串口名称
                     */
                    boolean flag = (boolean) table.getValueAt(i, 0);
                    if (flag) {
                        BaseUnitBean unit = unitService.getUnitBeanByUnittypeAndUnitnum(unit_type, (Byte) table.getValueAt(i, 1));
                        if (unit == null) {
                            continue;
                        }
                        units.add(unit);
                    }
                }
                if (units.size() == 0) {
                    errorMessage("请先选择要删除的单元!");
                    return;
                }
                if (unitService.deleteOnetypeUnit(unit_type, units)) {
                    successMessage("删除成功");
                    update();
                } else {
                    falseMessage("删除失败");
                }
            }
        });
    }

    public void update() {
        waterDataCollect.updateUnit();
        systemPanel2WaterInitTest.updateUnit();
        waterCollectStatesPanel.updateUnit();
        model.clearData();
        initTableData();
    }

    @Resource(name = "waterUnitTableRanderer")
    private TCR tcr;
    // 初始化表格

    private void initializeTable() {
        tcr.setCollect_name("水位");
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


    private void initTableData() {
        java.util.List<Vector<Object>> list = new ArrayList<>();
        java.util.List<BaseUnitBean> units = unitService.getUnitsByUnitType(unit_type);
        for (BaseUnitBean unit : units) {
            list.add(unit.getTableCol());
        }
        model.addDatas(list);

    }

}
