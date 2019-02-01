package com.thingtek.beanServiceDao.proinfo.dao;

import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;

public interface ProInfoDao {

    ProInfoBean find() throws Exception;

    boolean save(ProInfoBean proInfoBean) throws Exception;

}
