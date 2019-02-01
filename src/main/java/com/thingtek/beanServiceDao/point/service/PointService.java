package com.thingtek.beanServiceDao.point.service;


import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.point.dao.PointDao;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@Service
public class PointService extends BaseService {

    @Resource
    private PointDao dao;

    private List<PointBean> points;


    public Integer getPointNumByName(String point_name) {
        for (PointBean point :
                points) {
            if (Objects.equals(point.getPoint_name(), point_name)) {
                return point.getPoint_num();
            }
        }
        return null;
    }


    public Vector<String> getPointNames() {
        cache();
        Vector<String> vector = new Vector<>();
        for (PointBean point :
                points) {
            vector.add(point.getPoint_name());
        }
        return vector;
    }


    private void cache() {
        if (points == null || points.size() == 0) {
            try {
                points = dao.findAll();
            } catch (Exception e) {
                points = new ArrayList<>();
                log(e);
            }
        }
    }

}
