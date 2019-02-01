package com.thingtek.beanServiceDao.unit.baseunit.entity;

import com.thingtek.beanServiceDao.net.entity.NetBean;
import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import lombok.Data;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public @Data
abstract class BaseUnitBean {

    private NetBean net;//网关
    private String collect_type_name;//采集类型
    private Byte unit_type;//单元类型
    private Byte unit_num;//单元编号
    private String unit_name;//单元名
    //    private String unit_name_old;
    private Byte net_type;//网关类型
    private Byte net_num;//网关编号
    private Integer point_num;//位置编号
    private String point_name;//位置名
    private String place_name;//位置索引
    private String senser_type_name;//仪器类型
    private Integer senser_type_num;//仪器类型编号
    protected Map<String, Object> one;


    public BaseUnitBean() {
        one = new HashMap<>();
    }

    //    private String unit_class_name;
//    private String data_class_name;
//    private String view_class_name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BaseUnitBean that = (BaseUnitBean) o;

        return unit_type != null && unit_num != null && unit_type.equals(that.unit_type) && unit_num.equals(that.unit_num);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (unit_type != null ? unit_type.hashCode() : 0);
        result = 31 * result + (unit_num != null ? unit_num.hashCode() : 0);
        result = 31 * result + (point_name != null ? point_name.hashCode() : 0);
        return result;
    }

    public void resolve(Map<String, Object> one) {
        this.one = one;
        collect_type_name = (String) one.get("collect_type_name");
        unit_type = (byte) (int) (Integer) one.get("unit_type");
        unit_num = (byte) (int) (Integer) one.get("unit_num");
        net_type = one.get("net_type") == null
                ? 0
                : (byte) (int) (Integer) one.get("net_type");
        net_num = one.get("net_num") == null
                ? 1
                : (byte) (int) (Integer) one.get("net_num");
        point_num = (Integer) one.get("point_num");
        point_name = (String) one.get("point_name");
        place_name = (String) one.get("place_name");
        unit_name = one.get("unit_name") == null
                || ((String) one.get("unit_name")).trim().equals("")
                ? unit_num + "#"
                : (String) one.get("unit_name");
        senser_type_num = (Integer) one.get("senser_type_num");
        senser_type_name = (String) one.get("senser_type_name");

//        period = one.get("period") == null ? 120 : (int) one.get("period");
    }

    public void resolve2map() {
        one.put("unit_type", unit_type);
        one.put("unit_num", unit_num);
        one.put("net_type", net_type);
        one.put("net_num", net_num);
        one.put("point_num", point_num);
        one.put("place_name", place_name);
        one.put("unit_name", unit_name);
        one.put("senser_type_num", senser_type_num);
    }

    public void resolveTable(JTable table, int row) {
        /*
            1 单元编号
            2 单元名称
            3 单元类型
            4 水尺常数
            5 位置
            6 位置索引
            7 串口名称
        */
//        unit_name_old = unit_name;
        unit_name = (String) table.getValueAt(row, 2);
        one.put("unit_name", unit_name);
//        if (unit_name_old.equals(unit_name)) {
//            unit_name_old = null;
//        }
        senser_type_name = (String) table.getValueAt(row, 3);
        one.put("senser_type_name", senser_type_name);
        point_name = (String) table.getValueAt(row, 5);
        one.put("unit_name", point_name);
        place_name = (String) table.getValueAt(row, 6);
        one.put("place_name", place_name);
    }

    public Vector<Object> getTableCol() {
        return new Vector<>();
    }

    public Vector<Object> getTestTableCol(){
        return new Vector<>();
    }



    public Object get(String key) {
        return one.get(key);
    }
}
