package com.thingtek.beanServiceDao.unit.entity;

import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

@EqualsAndHashCode(callSuper = true)
@Data
public class WaterUnitBean extends BaseUnitBean {

    private Float value_init;
    private String com;
    private Float first_value;
    private Float second_value;
    private Float third_value;
    private Float frequency;

    @Override
    public String toString() {
        return one.toString();
    }

    @Override
    public void resolve(Map<String, Object> one) {
        super.resolve(one);
        value_init = (Float) one.get("value_init");
        com = (String) one.get("com");
        first_value = (Float) one.get("first_value");
        second_value = (Float) one.get("second_value");
        third_value = (Float) one.get("third_value");
        frequency = (Float) one.get("frequency");
    }

    @Override
    public void resolve2map() {
        super.resolve2map();
        one.put("value_init", value_init);
        one.put("frequency", frequency);
    }

    @Override
    public void resolveTable(JTable table, int row) {
        super.resolveTable(table, row);

        String value_init_str = String.valueOf(table.getValueAt(row, 4) == null ? "" : table.getValueAt(row, 4));
        value_init = value_init_str == null || "".equals(value_init_str)
                ? null
                : Float.parseFloat(value_init_str);
        String frequency_str = String.valueOf(table.getValueAt(row, 7) == null ? "" : table.getValueAt(row, 7));
        frequency = frequency_str == null || "".equals(frequency_str)
                ? 1
                : Float.parseFloat(frequency_str);
        one.put("value_init", value_init);
        one.put("frequency", frequency);
    }

    @Override
    public Object get(String key) {
        return one.get(key);
    }

    @Override
    public Vector<Object> getTableCol() {
        Vector<Object> objects = super.getTableCol();
        objects.add(false);
        objects.add(getUnit_num());//设备地址
        objects.add(getUnit_name());//设备名称
        objects.add(getSenser_type_name());//设备类型名
        objects.add(value_init);//水尺常数
        objects.add(getPoint_name());//位置
        objects.add(getPlace_name());//位置索引
//        objects.add(com);
        objects.add(frequency);
        return objects;
    }

    @Override
    public Vector<Object> getTestTableCol() {
        Vector<Object> objects = super.getTestTableCol();
        objects.add(getUnit_num());
        objects.add(getUnit_name());
        objects.add(first_value);
        objects.add(second_value);
        objects.add(third_value);
        objects.add(getAveInitValue());
        return objects;
    }

    public float getAveInitValue() {
        float ave = 0;
        int value_sum = 0;
        if (first_value != null) {
            ave += first_value;
            value_sum++;
        }
        if (second_value != null) {
            ave += second_value;
            value_sum++;
        }
        if (third_value != null) {
            ave += third_value;
            value_sum++;
        }
        return ave / (value_sum != 0 ? value_sum : 1);
    }
}
