package com.thingtek.beanServiceDao.net.service;


import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.net.dao.NetDao;
import com.thingtek.beanServiceDao.net.entity.NetBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NetService extends BaseService {
    @Resource
    private NetDao dao;


    private List<NetBean> nets;

    public List<NetBean> getNets() {
        cache();
        return nets;
    }

    public NetBean getNetByTypeAndNum(byte type, byte num) {
        cache();
        for (NetBean net : nets) {
            if (net.getNet_type() == type && net.getNet_num() == num) {
                return net;
            }
        }
        return null;
    }

    public List<NetBean> getNetsByTypeAndNumbers(byte type, List<Byte> nums) {
        cache();
        List<NetBean> netBeans = new ArrayList<>();
        for (NetBean net : nets) {
            if (net.getNet_type() == type && nums.contains(net.getNet_num())) {
                netBeans.add(net);
            }
        }
        return netBeans;
    }

    private void cache() {
        if (nets == null || nets.size() == 0) {
            try {
                nets = dao.find();
            } catch (Exception e) {
                nets = new ArrayList<>();
                log(e);
            }
        }
    }

}
