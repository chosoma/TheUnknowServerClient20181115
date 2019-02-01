package com.thingtek.view.logo;

import java.util.Hashtable;
import java.util.Map;

import com.thingtek.beanServiceDao.net.entity.NetBean;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import lombok.Data;

public @Data
class LogoInfo {
    private String SoftName, CompanyName, CopyrightName;
    private Map<String, BasePanel> basePanelMap;
    private Map<String, Map<String, BaseSystemPanel>> setPanelMap;

    private Map<NetBean, Boolean> netConnect = new Hashtable<>();

    public void netconnect(NetBean netBean,boolean flag) {
        netConnect.put(netBean, flag);
    }

}
