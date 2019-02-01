package com.thingtek.com.entity;

import com.thingtek.beanServiceDao.data.entity.SpeedDataBean;
import com.thingtek.beanServiceDao.data.entity.WaterDataBean;
import com.thingtek.beanServiceDao.net.entity.NetBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class G2SSpeedUploadData extends BaseG2S {
    @Override
    public void resolve() {
        int datelength = agreementConfig.getDataLength(0, bytes);
        int dataoff = agreementConfig.getParameteroff();
        byte[] datas = new byte[datelength];
        System.arraycopy(bytes, dataoff, datas, 0, datelength);
        System.out.println(Arrays.toString(datas));
        int index = 0;
        byte net_type = datas[index++];
        byte net_num = datas[index++];
        NetBean netBean = new NetBean();
        netBean.setNet_type(net_type);
        netBean.setNet_num(net_num);
        logoInfo.netconnect(netBean, true);

        int onedatalength = agreementConfig.getOnedatalength();
        int unitsum = bytes2int(index, 2, datas);
        if (unitsum == 0 || datas.length < unitsum * onedatalength + 4) {
            return;
        }
        index += 2;
        List<SpeedDataBean> dataBeanList = new ArrayList<>();
        for (; index < datas.length; ) {
            byte[] onedata = new byte[onedatalength];
            System.arraycopy(datas, index, onedata, 0, onedatalength);
            index += onedatalength;
            SpeedDataBean dataBean = new SpeedDataBean();
            dataBean.resolve(onedata);
            byte unit_type = dataBean.getUnit_type();
            byte unit_num = dataBean.getUnit_num();
            dataBeanList.add(dataBean);
        }

        boolean saveflag = dataService.saveData(dataBeanList.toArray(new SpeedDataBean[0]));
        if (!saveflag) {
            return;
        }
    }


    @Override
    public byte[] getResult() {
        return new byte[0];
    }
}