package com.thingtek.beanServiceDao.data.entity;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.WaterUnitBean;

import java.util.Vector;

public class WaterDataBean extends BaseDataBean {

    private Float testValue;

    public Vector<Object> tableData() {
        Vector<Object> vector = new Vector<>();
        /*
        1.类型
        2.仪器名称
        3.水位
        4.时间
        */
        BaseUnitBean unitBean = getUnit();
        vector.add(getUnit().getCollect_type_name());
        vector.add(getUnit().getUnit_name());
        vector.add(getValue1());
        vector.add(getInserttime());
        return vector;
    }

    public float getTestValue() {
        return testValue;
    }

    public void resolve(byte[] datas) {
        super.resolve(datas);
        if (getValue1() == null) {
            return;
        }
        setUnit_type((byte) 1);
        WaterUnitBean unitBean = (WaterUnitBean) getUnit();
        float valueinit = unitBean == null || unitBean.getValue_init() == null ? 0 : unitBean.getValue_init();
        testValue = getValue1();
        setValue1((int) ((valueinit - testValue * proInfoBean.getVertical_value() / 1000) * 100) / 100.0f);
    }
}
