package com.thingtek.beanServiceDao.base.service;

import org.slf4j.LoggerFactory;

public abstract class BaseService {

    protected void log(Exception e) {
        e.printStackTrace();
        LoggerFactory.getLogger(this.getClass()).error(e.toString());
    }

    protected void logInfo(String object) {
        System.err.println(object);
        LoggerFactory.getLogger(this.getClass()).info(object);
    }


}
