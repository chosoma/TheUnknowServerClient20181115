package com.thingtek.com.entity;

import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.homePage.WaterCollectStatesPanel;

public class G2SWaterSetInit extends BaseG2S {

    @Override
    public void resolve() {
        super.resolve();
        WaterCollectStatesPanel statePanel = (WaterCollectStatesPanel) logoInfo.getBasePanelMap().get("主　　页");
        if (statePanel.isCollect()) {
            statePanel.collect();
        }
        /*WaterDataCollectPanel dataCollect = (WaterDataCollectPanel) logoInfo.getBasePanelMap().get("水位采集");
        dataCollect.setStartCollect();*/

    }

    @Override
    public byte[] getResult() {
        return super.getResult();
    }

}
