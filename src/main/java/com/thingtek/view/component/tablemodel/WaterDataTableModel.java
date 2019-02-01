package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

@Component
public class WaterDataTableModel extends BaseTableModel {

    public WaterDataTableModel() {
        super();
        typeArray = new Class[]{
                String.class,
                String.class,
                Float.class,
                Date.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("设备类型");
        column.add("设备名称");
        column.add("水位");
        column.add("时间");
        this.setDataVector(row, column);
    }

}
