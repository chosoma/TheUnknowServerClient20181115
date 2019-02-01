package com.thingtek.view.component.dialog;

import com.thingtek.view.component.dialog.base.BaseDialog;
import com.thingtek.view.component.dialog.base.BaseSetDialog;
import com.thingtek.view.shell.dataCollect.WaterDataCollectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CollectPeriodDialog extends BaseSetDialog {

    private WaterDataCollectPanel collect;

    public void setCollect(WaterDataCollectPanel collect) {
        this.collect = collect;
    }

    public CollectPeriodDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    @Override
    public CollectPeriodDialog initDialog() {
        super.initDialog();

        xstart = 0;
        ystart = 10;
        JLabel jl11 = new JLabel("采集周期:", JLabel.CENTER);
        addCenter(jl11, xstart, ystart, widthlabel, hcomponent);

        xstart = 90;
        final JTextField jt21 = new JTextField(String.valueOf(collect.getPeriod()));
        addCenter(jt21, xstart, ystart, widthinput - 30, hcomponent);

        xstart = 190;
        JLabel jl31 = new JLabel("秒",JLabel.CENTER);
        addCenter(jl31,xstart,ystart,30,hcomponent);

        buttonSave.setText("保存");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String period_str = jt21.getText();
                if (!isNum(period_str)) {
                    errorMessage("请输入有效采集周期");
                    return;
                }
                long period = Long.parseLong(period_str);
                collect.setPeriod(period);
                dispose();
            }
        });

        setTotalSize(1);
        return this;
    }

    @Override
    protected boolean isNum(String str) {
        return str.matches("^([1-9]\\d*)$");
    }
}
