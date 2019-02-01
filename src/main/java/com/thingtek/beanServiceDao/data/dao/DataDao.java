package com.thingtek.beanServiceDao.data.dao;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.com.data.entity.DataSearchPara;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataDao {

    List<Map<String, Object>> findByPara(DataSearchPara para) throws Exception;

    boolean deleteDatas(@Param("unit") BaseUnitBean unit, @Param("dates") List<Date> dates) throws Exception;

    boolean saveData(BaseDataBean dataBean) throws Exception;

    boolean saveGoo(BaseDataBean dataBean) throws Exception;

}
