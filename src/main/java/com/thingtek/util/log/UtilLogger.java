package com.thingtek.util.log;

import org.apache.log4j.Logger;

public class UtilLogger {

    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz);
    }

}
