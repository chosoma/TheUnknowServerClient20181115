package com.thingtek.beanServiceDao.connect.dao;

import com.thingtek.beanServiceDao.connect.entity.ConnectBean;

import java.sql.SQLException;
import java.util.List;

public interface ConnectDao {
    List<ConnectBean> findAll() throws SQLException;
}
