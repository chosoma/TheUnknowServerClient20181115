package com.thingtek.com.entity;

import com.thingtek.beanServiceDao.net.entity.NetBean;
import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.homePage.WaterCollectStatesPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.SystemPanel2WaterInitTest;

/*
测试连接
 */
public class G2SWaterNetConnect extends BaseG2S {
    @Override
    public void resolve() {
        int datalength = agreementConfig.getDataLength(0, bytes);
        int paraoff = agreementConfig.getParameteroff();
        byte[] data = new byte[datalength];
        System.arraycopy(bytes, paraoff, data, 0, datalength);
        for (int i = 0; i < data.length; ) {
            byte nettype = data[i++];
            byte netnum = data[i++];
            NetBean netBean = new NetBean();
            netBean.setNet_type(nettype);
            netBean.setNet_num(netnum);
            logoInfo.netconnect(netBean, true);
        }


        SystemPanel2WaterInitTest initTest = (SystemPanel2WaterInitTest) logoInfo.getSetPanelMap().get("系统设置").get("水尺常数率定");
        WaterDataCollectPanel dataCollect = (WaterDataCollectPanel) logoInfo.getBasePanelMap().get("水位采集");
        WaterCollectStatesPanel statePanel = (WaterCollectStatesPanel) logoInfo.getBasePanelMap().get("主　　页");
        if (initTest.isCollect()) {
            initTest.stopTestConnect();
            initTest.collect();
            return;
            //率定中 其他不需要
        }

        if (dataCollect.isCollect()) {
            dataCollect.stopTestConnect();
            dataCollect.collect();
            return;
        }

        if (statePanel.isCollect()) {
            statePanel.stopTestConnect();
            statePanel.setInit();
            //主页的数据采集
        }
    }

    @Override
    public byte[] getResult() {
        return new byte[0];
    }

}
