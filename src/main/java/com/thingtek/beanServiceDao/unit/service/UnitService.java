package com.thingtek.beanServiceDao.unit.service;


import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.baseunit.entity.SenserUnitBean;
import com.thingtek.beanServiceDao.unit.dao.UnitDao;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.CollectUnitBean;

import com.thingtek.config.ClassConfig;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UnitService extends BaseService {

    @Resource
    private UnitDao dao;

    @Resource
    private PointService pointService;

    @Resource
    private ClassConfig classConfig;
    private List<BaseUnitBean> units;
    private List<CollectUnitBean> collects;
    private List<SenserUnitBean> sensers;

    public boolean saveUnit(BaseUnitBean unitBean) {
        boolean flag = false;
        try {
            unitBean.setPoint_num(pointService.getPointNumByName(unitBean.getPoint_name()));
            unitBean.setSenser_type_num(getSenserNumBySenserName(unitBean.getSenser_type_name()));
            unitBean.resolve2map();
            flag = dao.saveUnit(unitBean) && dao.saveOnetypeUnit(unitBean.getUnit_type(), unitBean);
        } catch (Exception e) {
            log(e);
        }
        if (flag) {
            units.clear();
            cache();
        }
        return flag;
    }

    public List<BaseUnitBean> getAll() {
        cache();
        return units;
    }

    public List<BaseUnitBean> getUnitsByUnitType(byte type) {
        cache();
        List<BaseUnitBean> units = new ArrayList<>();
        for (BaseUnitBean unit : this.units) {
            if (Objects.equals(unit.getUnit_type(), type)) {
                units.add(unit);
            }
        }
        return units;
    }

    public String getUnitNameByTypeAndNumber(byte type, byte number) {
        cache();
        for (BaseUnitBean unit : units) {
            if (Objects.equals(unit.getUnit_type(), type) && Objects.equals(unit.getUnit_num(), number)) {
                return unit.getUnit_name();
            }
        }
        return "";
    }

    public Vector<Integer> getUnHasUnitNum(Byte unittype) {
        cache();
        Vector<Integer> vector = new Vector<>();
        for (int i = 1; i <= 255; i++) {
            vector.add(i);
        }
        Vector<Integer> removes = new Vector<>();
        for (BaseUnitBean unit : units) {
            if (Objects.equals(unit.getUnit_type(), unittype)) {
                removes.add(unit.getUnit_num() & 0xff);
            }
        }
        vector.removeAll(removes);
        return vector;
    }


    public Byte getCollectNumByCollectName(String collect_name) {
        cache();
        for (CollectUnitBean unitNameBean : collects) {
            if (collect_name.equals(unitNameBean.getCollect_type_name())) {
                return unitNameBean.getCollect_type_num();
            }
        }
        return null;
    }


    public Vector<String> getCollectNames() {
        cache();
        Vector<String> vector = new Vector<>();
        for (CollectUnitBean name : collects) {
            vector.add(name.getCollect_type_name());
        }
        return vector;
    }


    public Vector<String> getUnitNamesByCollectName(String collect_name) {
        cache();
        Vector<String> unitNames = new Vector<>();
        List<BaseUnitBean> units = getUnitsByCollectName(collect_name);
        for (BaseUnitBean unit : units) {
            unitNames.add(unit.getUnit_name());
        }

        return unitNames;
    }

    public Vector<String> getSensers() {
        cache();
        Vector<String> vector = new Vector<>();
        for (SenserUnitBean type :
                sensers) {
            vector.add(type.getSenser_type_name());
        }
        return vector;
    }

    public int getSenserNumBySenserName(String senser_name) {
        for (SenserUnitBean senser : sensers) {
            if (Objects.equals(senser.getSenser_type_name(), senser_name)) {
                return senser.getSenser_type_num();
            }
        }
        return 0;
    }

    public List<BaseUnitBean> getUnitsByCollectName(String collect_name) {
        cache();
        List<BaseUnitBean> units = new ArrayList<>();
//        System.out.println("collect_name1:"+collect_name);
        for (BaseUnitBean unit : this.units) {
//            System.out.println("collect_name2:"+unit.getCollect_type_name());
            if (Objects.equals(unit.getCollect_type_name(), collect_name)) {
                units.add(unit);
            }
        }
        return units;
    }


    public BaseUnitBean getUnitBeanByUnittypeAndUnitnum(Byte unit_type, Byte unit_num) {
        cache();
        for (BaseUnitBean unit : units) {
            if (Objects.equals(unit_type, unit.getUnit_type()) && Objects.equals(unit_num, unit.getUnit_num())) {
                return unit;
            }
        }
        return null;
    }

    public BaseUnitBean getUnitByUnitName(String collect_name, String unit_name) {
        cache();
        for (BaseUnitBean unit : units) {
            if (Objects.equals(unit.getUnit_name(), unit_name) && Objects.equals(unit.getCollect_type_name(), collect_name)) {
                return unit;
            }
        }
        return null;
    }

    public BaseUnitBean getNewUnitByType(byte unit_type) {
        BaseUnitBean unitBean = null;
        try {
            unitBean = (BaseUnitBean) this.getClass()
                    .getClassLoader()
                    .loadClass(classConfig.getUnitClass(unit_type & 0xff))
                    .newInstance();
            unitBean.setUnit_type((byte) unit_type);
        } catch (Exception e) {
            log(e);
        }
        return unitBean;
    }

    public boolean existsName(byte unit_type, String unit_name) {
        cache();
        for (BaseUnitBean unit : getUnitsByUnitType(unit_type)) {
            if (Objects.equals(unit.getUnit_name(), unit_name)) {
                return true;
            }
        }
        return false;
    }


    public boolean deleteOnetypeUnit(byte unit_type, List<BaseUnitBean> units) {
        boolean flag;
        try {
            flag = dao.deleteOnetypeUnit(unit_type, units);
        } catch (Exception e) {
            log(e);
            flag = false;
        }
        if (flag) {
            this.units.removeAll(units);
        }
        return flag;
    }

    public boolean updateAll(byte unit_type) {
        boolean flag = false;
        List<BaseUnitBean> unitBeanList = getUnitsByUnitType(unit_type);
        try {
            flag = dao.saveUnit(unitBeanList.toArray(new BaseUnitBean[0])) &&
                    dao.saveOnetypeUnit(unit_type, unitBeanList.toArray(new BaseUnitBean[0]));
        } catch (Exception e) {
            log(e);
        }
        return flag;
    }

    private void cache() {
        if (units == null || units.size() == 0) {
            units = new ArrayList<>();
            List<Map<String, Object>> units;
            try {
                units = dao.findAll();
            } catch (Exception e) {
                units = new ArrayList<>();
                log(e);
            }
            for (Map<String, Object> one : units) {
                try {
                    BaseUnitBean unitBean = (BaseUnitBean) this.getClass()
                            .getClassLoader()
                            .loadClass(classConfig.getUnitClass((Integer) one.get("collect_type_num")))
                            .newInstance();
                    unitBean.resolve(one);
                    this.units.add(unitBean);
                } catch (Exception e) {
                    log(e);
                }
            }
//            System.out.println(units);
        }
        if (collects == null || collects.size() == 0) {
            try {
                collects = dao.findCollects();
            } catch (Exception e) {
                collects = new ArrayList<>();
                log(e);
            }
        }
        if (sensers == null || sensers.size() == 0) {
            try {
                sensers = dao.findSensers();
            } catch (Exception e) {
                sensers = new ArrayList<>();
                log(e);
            }
        }
    }
}
