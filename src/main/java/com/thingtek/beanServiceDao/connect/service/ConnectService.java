package com.thingtek.beanServiceDao.connect.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.connect.dao.ConnectDao;
import com.thingtek.beanServiceDao.connect.entity.ConnectBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectService extends BaseService {

    @Resource
    private ConnectDao dao;

    public List<ConnectBean> getConnects() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            log(e);
            return new ArrayList<>();
        }
    }

}
