package com.thingtek.beanServiceDao.proinfo.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.proinfo.dao.ProInfoDao;
import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProInfoService extends BaseService {

    @Resource
    private ProInfoDao dao;

    private ProInfoBean proInfo;

    public ProInfoBean getInfo() {
        try {
            if (proInfo == null) {
                proInfo = dao.find();
            }
        } catch (Exception e) {
            log(e);
        }
        return proInfo;
    }


    public boolean save(ProInfoBean proInfoBean) {
        try {
            proInfo = proInfoBean;
            return dao.save(proInfoBean);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }

}
