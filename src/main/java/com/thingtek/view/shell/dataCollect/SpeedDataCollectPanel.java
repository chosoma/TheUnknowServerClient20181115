package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.com.entity.BaseS2G;
import com.thingtek.config.agreement.AgreementConfig;
import com.thingtek.serialPort.SerialConnect;
import com.thingtek.view.shell.BasePanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class SpeedDataCollectPanel extends BasePanel
//        implements Collect
{

    public SpeedDataCollectPanel() {
        authority = UserService.UP;
    }

    private byte unit_type = 0;
    @Resource
    private AgreementConfig agreementConfig;
    @Resource
    private SerialConnect connect;

    private boolean collect;

    public boolean isCollect() {
        return collect;
    }

    @Override
    public SpeedDataCollectPanel init() {
        setOpaque(false);
        JButton test = new JButton("测试连接");
        test.setOpaque(false);
        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testConnect();
            }
        });
        this.add(test);
        JButton start = new JButton("开始采集");
        start.setOpaque(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collect();
            }
        });
        this.add(start);
        JButton stop = new JButton("停止采集");
        stop.setOpaque(false);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopCollect();
            }
        });
        this.add(stop);
        return this;
    }

    private int times = 1;
    private java.util.Timer timer;

    private void testConnect() {
        collect = true;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                BaseS2G s2g = agreementConfig.getS2G("speednetconnect");
                s2g.resolve();
                connect.send(s2g.getResult());
                times++;
                if (times >= 4) {
                    falseMessage("连接超时,请检查设备连接!");
                    times = 1;
                    timer.cancel();
                    timer = null;
                    collect = false;
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void collect() {
        collect = true;
        times = 1;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        BaseS2G s2g = agreementConfig.getS2G("speedteststart");
        byte[] datas = new byte[]{0, 0};
        s2g.setDatas(datas);
        s2g.resolve();
        connect.send(s2g.getResult());
    }

    public void stopCollect() {
        BaseS2G s2g = agreementConfig.getS2G("speedteststop");
        byte[] datas = new byte[]{0, 0};
        s2g.setDatas(datas);
        s2g.resolve();
        connect.send(s2g.getResult());
        collect = false;
    }

}
