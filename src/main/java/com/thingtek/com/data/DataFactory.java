package com.thingtek.com.data;


import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.util.FormatTransfer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DataFactory {

    private List<BaseDataBean> dataList = new ArrayList<BaseDataBean>();

    // 将数据集合中的数据保存到数据库中
    public synchronized void saveData() {
        try {
            if (dataList.size() > 0) {
                dataList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.gc();
        }
    }


    public void processData(byte[] data, Date time) {
        byte gatewayType = data[1];// 网关类型
        byte gatewaynumber = data[2];// 网关ID

        int length = FormatTransfer.getDataLen(data[5], data[6]);// 总数据长度

        int dataCount = data[7];// 数据条数
        int off = 7;
        int count = 0;
        for (int i = 0; i < dataCount; i++) {
//			System.out.println("count:"+count++);
            try {
                byte unitType = data[++off];// 单元类型
                byte unitNumber = data[++off];// 单元ID
                off++;
                byte[] bytes1 = new byte[4];
                System.arraycopy(data, off, bytes1, 0, 4);
                off += 4;
                byte[] bytes2 = new byte[4];
                System.arraycopy(data, off, bytes2, 0, 4);
                off += 4;
                byte[] bytes3 = new byte[4];
                System.arraycopy(data, off, bytes3, 0, 4);
                off += 4;
                float dy = data[off] / 10.0f;


            } catch (ArrayIndexOutOfBoundsException aioobe) {
                aioobe.printStackTrace();
            }
        }
        System.gc();

    }

    private void checkData(BaseDataBean dataBean) {
        if (dataList.contains(dataBean)) {
            for (BaseDataBean d : dataList) {
                if (d.equals(dataBean)) {
//                    if (d.isLowPres() != dataBean.isLowPres()) {
//                        plusDate(dataBean);
//                    } else if (d.isLowLock() != dataBean.isLowLock()) {
//                        plusDate(dataBean);
//                    } else {
//                        dataList.remove(dataBean);
//                    }
                    break;
                }
            }
            checkData(dataBean);
        }
    }


    private void plusDate(BaseDataBean dataBean) {
        Date date1 = Calendar.getInstance().getTime();
        Date date = dataBean.getInserttime();
        long timelong = date.getTime();
        timelong += 1000;
        date1.setTime(timelong);
        dataBean.setInserttime(date1);
    }

}
