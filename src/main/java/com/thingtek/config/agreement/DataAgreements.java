package com.thingtek.config.agreement;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public @Data
class DataAgreements {

    //    单元类型
    private byte type;
    //    类型下标
    private int typeoff;
    //    列名
    private String[] tableProperties;
    //    数值参数
    private List<DataAgreement> agreements;

    public void resolve(Map<String, Object> one, byte[] datas, Date date) {
        if (datas[typeoff] != type) return;


    }


}
