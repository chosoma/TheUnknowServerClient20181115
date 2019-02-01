package com.thingtek.com.data.entity;

import com.thingtek.beanServiceDao.user.entity.UserBean;
import lombok.Data;

import java.util.Date;

public @Data
class DataSearchPara {

    private Byte unit_type, unit_num;

    private Date t1, t2;

    private UserBean user;

    private int startcount;

//    private List<BaseUnitBean> units;


}
