package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Vector;

@Component
public class SpeedDataTableModel extends BaseTableModel {

    public SpeedDataTableModel() {
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
        column.add("流速");
        column.add("时间");
        this.setDataVector(row, column);
    }

}
