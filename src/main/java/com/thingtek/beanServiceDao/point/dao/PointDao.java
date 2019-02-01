package com.thingtek.beanServiceDao.point.dao;

import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.user.entity.UserBean;

import java.util.List;

public interface PointDao {

    List<PointBean> findAll() throws Exception;

}
