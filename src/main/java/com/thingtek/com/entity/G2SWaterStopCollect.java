package com.thingtek.com.entity;

import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.SystemPanel2WaterInitTest;

public class G2SWaterStopCollect extends BaseG2S implements NetConnect {

    @Override
    public void resolve() {

        int datalength = agreementConfig.getDataLength(0, bytes);
        int paraoff = agreementConfig.getParameteroff();
        byte[] data = new byte[datalength];
        System.arraycopy(bytes, paraoff, data, 0, datalength);
        WaterDataCollectPanel dataCollect = (WaterDataCollectPanel) logoInfo.getBasePanelMap().get("水位采集");
        SystemPanel2WaterInitTest initTest = (SystemPanel2WaterInitTest) logoInfo.getSetPanelMap().get("系统设置").get("水尺常数率定");

        for (int i = 0; i < data.length; ) {
            byte net_type = data[i++];
            byte net_num = data[i++];
            byte unit_type = data[i++];
            byte unit_num = data[i++];
            if (initTest.isCollect()) {
                initTest.addStop(unit_type, unit_num);
                continue;
                //率定中 其他不需要
            }
            dataCollect.addStop(unit_type, unit_num);
        }

    }

    @Override
    public byte[] getResult() {
        return new byte[0];
    }

}
