package com.thingtek.beanServiceDao.proinfo.entity;

import lombok.Data;

@Data
public class ProInfoBean {

    private String pro_name;
    private Double vertical_value;//垂直比尺
    private Double horizontal_value;//水平比尺
    private String manager;
    private String tel_num;
    private String com;

    private Double first_value;//一次用户输入
    private Double second_value;//二次用户输入
    private Double third_value;//三次用户输入
    private int test_count;

}
