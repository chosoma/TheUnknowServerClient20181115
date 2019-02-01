package com.thingtek.config;

import lombok.Data;

import java.util.Map;

public @Data
class ClassConfig {
    private Map<String, String> unitClassConfigMap;
    private Map<String, String> dataClassConfigMap;

    public String getUnitClass(Integer unit_type) {
        return unitClassConfigMap.get(String.valueOf(unit_type));
    }

    public String getDataClass(Integer unit_type) {
        return dataClassConfigMap.get(String.valueOf(unit_type));
    }

}
