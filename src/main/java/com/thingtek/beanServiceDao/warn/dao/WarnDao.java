package com.thingtek.beanServiceDao.warn.dao;

import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;

import java.util.List;

public interface WarnDao {


    boolean saveAll(List<WarnBean> warnBeans) throws Exception;

    List<WarnBean> findByUnit(BaseUnitBean unit) throws Exception;

    boolean deleteOne(WarnBean warnBean) throws Exception;

}
