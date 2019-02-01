package com.thingtek.beanServiceDao.warn.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.warn.dao.WarnDao;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

@Service
public class WarnService extends BaseService{

    @Resource
    private WarnDao dao;


    public boolean saveWarn(WarnBean... warnBeans) {
        List<WarnBean> warnBeanList = Arrays.asList(warnBeans);

        try {
            return dao.saveAll(warnBeanList);
        } catch (Exception e) {
            log(e);
            return false;
        }

    }

    public List<WarnBean> getWarn(BaseUnitBean unit) {

        try {
            return dao.findByUnit(unit);
        } catch (Exception e) {
            log(e);
            return new ArrayList<>();
        }

    }

    public boolean delWarn(WarnBean... warnBeans) {
        Map<WarnBean, Boolean> map = new HashMap<>();
        for (WarnBean warn : warnBeans) {
            boolean flag;
            try {
                flag = dao.deleteOne(warn);
            } catch (Exception e) {
                log(e);
                flag = false;
            }
            if (!flag) {
                map.put(warn, false);
            }
        }
        return map.size() == 0;
    }


}
