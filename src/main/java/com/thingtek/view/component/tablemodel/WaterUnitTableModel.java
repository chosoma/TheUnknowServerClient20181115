package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Vector;

@Component
public class WaterUnitTableModel extends BaseTableModel {

    public WaterUnitTableModel() {
        super();
        typeArray = new Class[]{
                Boolean.class,
                Number.class,
                String.class,
                JComboBox.class,
                Float.class,
                JComboBox.class,
                String.class,
                Float.class
//                ,
//                JComboBox.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();

        Vector<String> column = new Vector<String>();
        column.add(" ");
        column.add("设备地址");
        column.add("设备名称");
        column.add("设备类型");
        column.add("水尺常数(米)");
        column.add("位置");
        column.add("位置索引");
        column.add("探针频率(秒/次)");
//        column.add("串口");
        this.setDataVector(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 1;
    }
}

