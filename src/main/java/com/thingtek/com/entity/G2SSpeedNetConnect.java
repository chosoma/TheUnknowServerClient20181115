package com.thingtek.com.entity;

import com.thingtek.beanServiceDao.net.entity.NetBean;
import com.thingtek.view.shell.dataCollect.SpeedDataCollectPanel;

public class G2SSpeedNetConnect extends BaseG2S implements NetConnect {

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
        SpeedDataCollectPanel datacollect = (SpeedDataCollectPanel) logoInfo.getBasePanelMap().get("流速采集");
        if (datacollect.isCollect()) {
            datacollect.collect();
        }


    }

    @Override
    public byte[] getResult() {
        return new byte[0];
    }
}
