package com.thingtek.beanServiceDao.warn.entity;


import lombok.Data;

import java.util.Date;

public @Data
class WarnBean {

    private Integer id;
    private Byte unit_type;
    private Byte unit_num;
    private String warn_info;
    private Date warning_time;
    private Boolean state;
    private Integer point_num;
    private String point_name;
    private String phase;

}
