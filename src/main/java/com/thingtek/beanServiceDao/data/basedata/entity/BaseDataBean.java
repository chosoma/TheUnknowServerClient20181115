package com.thingtek.beanServiceDao.data.basedata.entity;

import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.config.agreement.AgreementConfig;
import com.thingtek.config.agreement.DataAgreements;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

public @Data
abstract class BaseDataBean {

    private Map<String, Object> one;
    private BaseUnitBean unit;
    private Byte unit_type;
    private Byte unit_num;
    private Date inserttime;
    private AgreementConfig agreementConfig;
    private DataAgreements dataAgreement;
    private Float value1;
    private Float value2;
    private Float value3;
    protected ProInfoBean proInfoBean;

    public void resolve(Map<String, Object> one) {
        this.one = one;
        unit_type = (byte) (int) (Integer) one.get("unit_type");
        unit_num = (byte) (int) (Integer) one.get("unit_num");
        inserttime = (Date) one.get("inserttime");
        value1 = one.get("value1") != null ? (Float) one.get("value1") : 0.0F;
        value2 = one.get("value2") != null ? (Float) one.get("value2") : 0.0F;
        value3 = one.get("value3") != null ? (Float) one.get("value3") : 0.0F;
    }

    public void resolve(byte[] datas) {
        unit_type = datas[0];
        unit_num = datas[1];
        byte ff = (byte) 0xff;
        if (datas[2] == ff && datas[3] == ff && datas[4] == ff && datas[5] == ff) {
            value1 = null;
        } else {
            value1 = bytes2int(2, 4, datas) / 100.0f;
        }
    }

    public void resolveByAgreement(byte[] datas, Date date) {
        if (one == null) {
            one = new HashMap<>();
        }
        for (DataAgreements dataAgreement : agreementConfig.getDataAgreements()) {
            dataAgreement.resolve(one, datas, date);
        }
    }

    public Vector<Object> tableData() {
        Vector<Object> vector = new Vector<>();
        return vector;
    }

    protected float bytes2float(byte[] b, int off, int length, int scale) {
        int temp = 0;
        for (int i = off, index = 0; i < off + length; i++, index++) {
            temp |= (b[i] & 0xFF) << (8 * index);
        }
        Float f = Float.intBitsToFloat(temp);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    protected int bytes2int(int off, int length, byte[] bytes) {
        int i = 0;
        for (int j = 0; j < length; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }


    public float newScale(float f1, float f2, int scale) {
        float f3 = f1 - f2;
        int i = (int) Math.round(f3 * Math.pow(10, scale));
        return (float) (i / Math.pow(10, scale));
    }

}
