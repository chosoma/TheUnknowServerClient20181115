package com.thingtek.view.index;


import lombok.Data;
//import com.thingtek.util.JfreeChartUtil;
import com.thingtek.view.login.Login;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public @Data
class Index {

    @Resource
    private Login login;

    public void init() {
//        JfreeChartUtil.setChartTheme();
        login.init();
    }

}
