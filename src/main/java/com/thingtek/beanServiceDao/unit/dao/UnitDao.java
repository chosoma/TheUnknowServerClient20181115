package com.thingtek.beanServiceDao.unit.dao;

import com.thingtek.beanServiceDao.net.entity.NetBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.CollectUnitBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.SenserUnitBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Vector;


public interface UnitDao {

    List<BaseUnitBean> findUnitsByNet(NetBean net) throws Exception;

    BaseUnitBean findUnitByPoint_nameAndPhase(String point_name, String phase) throws Exception;

    List<Map<String, Object>> findAll() throws Exception;

    List<CollectUnitBean> findCollects() throws Exception;

    List<SenserUnitBean> findSensers() throws Exception;

    Vector<String> findType_names() throws Exception;

    boolean saveUnit(BaseUnitBean... unitBean) throws Exception;

    boolean saveOnetypeUnit(@Param("unit_type") byte unit_type, @Param("array") BaseUnitBean... unitBean) throws Exception;

    boolean deleteOnetypeUnit(@Param("unit_type") byte unit_type, @Param("array") List<BaseUnitBean> unitBean) throws Exception;

    boolean updateUnit(Map<String, Object> one) throws Exception;

    void updateAll(List<BaseUnitBean> units) throws Exception;

}
