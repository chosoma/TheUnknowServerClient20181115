package com.thingtek.beanServiceDao.net.dao;

import com.thingtek.beanServiceDao.net.entity.NetBean;

import java.sql.SQLException;
import java.util.List;

public interface NetDao {

    List<NetBean> find()throws SQLException;

}
