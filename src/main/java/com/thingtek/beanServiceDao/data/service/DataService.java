package com.thingtek.beanServiceDao.data.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.data.dao.DataDao;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.com.data.entity.DataSearchPara;
import com.thingtek.config.ClassConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataService extends BaseService {

    @Resource
    private DataDao dao;

    @Resource
    private UnitService unitService;

    @Resource
    private ClassConfig classConfig;

    public List<BaseDataBean> getDataByPara(DataSearchPara para) {
        List<Map<String, Object>> datas;
        try {
            datas = dao.findByPara(para);
        } catch (Exception e) {
            datas = new ArrayList<>();
            log(e);
        }
        List<BaseDataBean> baseDataBeanList = new ArrayList<>();
        for (Map<String, Object> one : datas) {
            try {
                BaseDataBean baseDataBean = (BaseDataBean) this.getClass()
                        .getClassLoader()
                        .loadClass(classConfig.getDataClass((Integer) one.get("unit_type")))
                        .getConstructor()
                        .newInstance();
                baseDataBean.resolve(one);
                baseDataBean.setUnit(unitService.getUnitBeanByUnittypeAndUnitnum(baseDataBean.getUnit_type(), baseDataBean.getUnit_num()));
                baseDataBeanList.add(baseDataBean);
            } catch (Exception e) {
                log(e);
            }
        }
        return baseDataBeanList;
    }

    public boolean deleteData(Map<BaseUnitBean, List<Date>> datas) {
        List<Boolean> flags = new ArrayList<>();
        Set<Map.Entry<BaseUnitBean, List<Date>>> entries = datas.entrySet();
        for (Map.Entry<BaseUnitBean, List<Date>> entry : entries) {
            /*Map<String, Object> map = new HashMap<>();
            map.put("unit", entry.getKey());
            map.put("dates", entry.getValue());*/
//            System.out.println(entry.getKey());
            try {
                flags.add(dao.deleteDatas(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                flags.add(false);
                log(e);
            }
        }
        return !flags.contains(false);
    }

    public boolean saveData(BaseDataBean... datas) {
        List<Boolean> flags = new ArrayList<>();
        for (BaseDataBean data : datas) {
            try {
                flags.add(dao.saveGoo(data) && dao.saveData(data));
            } catch (Exception e) {
                log(e);
                flags.add(false);
            }
        }
        return !flags.contains(false);
    }


}
