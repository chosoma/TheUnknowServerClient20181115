package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Vector;

@Component
public class WaterUnitTestTableModel extends BaseTableModel {

    public WaterUnitTestTableModel() {
        super();
        typeArray = new Class[]{
                Number.class,
                String.class,
                Float.class,
                Float.class,
                Float.class,
                Float.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<>();
        column.add("设备地址");
        column.add("设备名称");
        column.add("第一次率定");
        column.add("第二次率定");
        column.add("第三次率定");
        column.add("率定平均值");
        this.setDataVector(row, column);
    }

}
