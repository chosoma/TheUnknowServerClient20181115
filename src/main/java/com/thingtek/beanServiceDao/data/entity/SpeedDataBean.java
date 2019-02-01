package com.thingtek.beanServiceDao.data.entity;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;

import java.util.Vector;

public class SpeedDataBean extends BaseDataBean {

    public Vector<Object> tableData() {
        Vector<Object> vector = new Vector<>();
        /*
        1.类型
        2.仪器名称
        3.流速
        4.时间
        */
        vector.add(getUnit().getCollect_type_name());
        vector.add(getUnit().getUnit_name());
        vector.add(getValue1());
        vector.add(getInserttime());
        return vector;
    }

}
