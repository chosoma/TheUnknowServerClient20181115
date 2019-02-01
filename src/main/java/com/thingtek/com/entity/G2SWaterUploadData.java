package com.thingtek.com.entity;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.data.entity.WaterDataBean;
import com.thingtek.beanServiceDao.net.entity.NetBean;
import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;
import com.thingtek.view.shell.homePage.WaterCollectStatesPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.SystemPanel2WaterInitTest;

import java.util.ArrayList;
import java.util.List;

public class G2SWaterUploadData extends BaseG2S {
    @Override
    public void resolve() {
        int datelength = agreementConfig.getDataLength(0, bytes);
        int dataoff = agreementConfig.getParameteroff();
        byte[] datas = new byte[datelength];
        System.arraycopy(bytes, dataoff, datas, 0, datelength);
        int index = 0;
        byte net_type = datas[index++];
        byte net_num = datas[index++];
        NetBean netBean = new NetBean();
        netBean.setNet_type(net_type);
        netBean.setNet_num(net_num);
        logoInfo.netconnect(netBean, true);

        int onedatalength = agreementConfig.getOnedatalength();
        List<WaterDataBean> dataBeanList = new ArrayList<>();
        for (; index < datas.length; ) {
            byte[] onedata = new byte[onedatalength];
            System.arraycopy(datas, index, onedata, 0, onedatalength);
            byte unit_type = onedata[0];
            byte unit_num = onedata[1];
            index += onedatalength;
            WaterDataBean dataBean = new WaterDataBean();
            dataBean.setUnit(unitService.getUnitBeanByUnittypeAndUnitnum((byte) 1, unit_num));
            dataBean.setProInfoBean(proInfoService.getInfo());
            dataBean.resolve(onedata);
            dataBeanList.add(dataBean);
        }

        SystemPanel2WaterInitTest initTest = (SystemPanel2WaterInitTest) logoInfo.getSetPanelMap().get("系统设置").get("水尺常数率定");
        WaterCollectStatesPanel statePanel = (WaterCollectStatesPanel) logoInfo.getBasePanelMap().get("主　　页");
        WaterDataCollectPanel dataCollect = (WaterDataCollectPanel) logoInfo.getBasePanelMap().get("水位采集");

        if (initTest != null && initTest.isCollect()) {
            for (WaterDataBean data : dataBeanList) {
                if (data.getValue1() == null) {
                    initTest.addStart(data.getUnit_type(), data.getUnit_num());
                } else {
                    initTest.addCollect(data);
                }
            }
            return;
            //率定中 数据不存储
        }


        for (WaterDataBean data : dataBeanList) {
            if (data.getValue1() == null) {
                if (dataCollect.isCollect()) {
                    dataCollect.addStart(data.getUnit_type(), data.getUnit_num());
                } else {
                    statePanel.addStart(data.getUnit_type(), data.getUnit_num());
                }
            } else {
                if (dataCollect.isCollect()) {
                    boolean saveflag = dataService.saveData(data);
                    if (!saveflag) {
                        continue;
                    }
                    dataCollect.addData(data);
                    statePanel.addData(data);
                } else {
                    statePanel.addData(data);
                }
            }
        }

    }

    @Override
    public byte[] getResult() {
        return null;
    }
}