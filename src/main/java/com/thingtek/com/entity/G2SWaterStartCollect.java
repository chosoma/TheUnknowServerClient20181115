package com.thingtek.com.entity;

import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.homePage.WaterCollectStatesPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.SystemPanel2WaterInitTest;

public class G2SWaterStartCollect extends BaseG2S {
    @Override
    public void resolve() {
        super.resolve();
        int datalength = agreementConfig.getDataLength(0, bytes);
        int paraoff = agreementConfig.getParameteroff();
        byte[] data = new byte[datalength];
        System.arraycopy(bytes, paraoff, data, 0, datalength);
        WaterDataCollectPanel dataCollect = (WaterDataCollectPanel) logoInfo.getBasePanelMap().get("水位采集");
        SystemPanel2WaterInitTest initTest = (SystemPanel2WaterInitTest) logoInfo.getSetPanelMap().get("系统设置").get("水尺常数率定");
        WaterCollectStatesPanel statePanel = (WaterCollectStatesPanel) logoInfo.getBasePanelMap().get("主　　页");

        for (int i = 0; i < data.length; ) {
            byte net_type = data[i++];
            byte net_num = data[i++];
            byte unit_type = data[i++];
            byte unit_num = data[i++];
            if (initTest.isCollect()) {
                initTest.removeStart(unit_type, unit_num);
                continue;
                //率定中 其他不需要
            }
            if (dataCollect.isCollect()) {
                dataCollect.removeStart(unit_type, unit_num);
            } else if (statePanel.isCollect()) {
                statePanel.removeStart(unit_type, unit_num);
            }

        }


    }

    @Override
    public byte[] getResult() {
        return super.getResult();
    }
}
