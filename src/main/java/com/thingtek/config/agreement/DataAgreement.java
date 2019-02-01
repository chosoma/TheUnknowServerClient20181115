package com.thingtek.config.agreement;

import lombok.Data;

public @Data
class DataAgreement {

    private String propertyname;
    private int off;
    private int length;
    private int scale;

    private String dateType;
    private Float operation;

}
