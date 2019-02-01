package com.thingtek.view.component.dialog;

import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.entity.WaterUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.view.component.dialog.base.BaseSetDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnitDialog extends BaseSetDialog {

    public UnitDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    private UnitService unitService;

    private PointService pointService;

    private byte unittype;

    public void setUnittype(byte unittype) {
        this.unittype = unittype;
    }

    @Override
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    public void setPointService(PointService pointService) {
        this.pointService = pointService;
    }

    @Override
    public UnitDialog initDialog() {
        super.initDialog();

        xstart = 0;
        ystart = 10;
        JLabel jl11 = new JLabel("设备地址", JLabel.CENTER);
        addCenter(jl11, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jl12 = new JLabel("设备名称", JLabel.CENTER);
        addCenter(jl12, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jl13 = new JLabel("设备类型", JLabel.CENTER);
        addCenter(jl13, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jl14 = new JLabel("水尺常数", JLabel.CENTER);
        addCenter(jl14, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jl15 = new JLabel("设备位置", JLabel.CENTER);
        addCenter(jl15, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jl16 = new JLabel("位置索引", JLabel.CENTER);
        addCenter(jl16, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jl17 = new JLabel("采集频率", JLabel.CENTER);
        addCenter(jl17, xstart, ystart, widthlabel, hcomponent);

        xstart = 90;
        ystart = 10;

        final JComboBox<Integer> jc21 = new JComboBox<>(unitService.getUnHasUnitNum(unittype));
        addCenter(jc21, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;
        final JTextField jt22 = new JTextField();
        addCenter(jt22, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;
        final JComboBox<String> jc23 = new JComboBox<>(unitService.getSensers());
        addCenter(jc23, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;
        final JTextField jt24 = new JTextField();
        addCenter(jt24, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;
        final JComboBox<String> jc25 = new JComboBox<>(pointService.getPointNames());
        addCenter(jc25, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;
        final JTextField jt26 = new JTextField();
        addCenter(jt26, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;
        final JTextField jt27 = new JTextField();
        addCenter(jt27, xstart, ystart, widthinput, hcomponent);

        setTotalSize(7);
        buttonSave.setText("添加");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WaterUnitBean unitBean = (WaterUnitBean) unitService.getNewUnitByType(unittype);
                if (unitBean == null) {
                    falseMessage("保存失败");
                    return;
                }
                String unit_name = jt22.getText();
                if (unitService.existsName(unittype, unit_name)) {
                    falseMessage("设备名称存在请重新输入");
                    return;
                }
                Integer unit_num = (Integer) jc21.getSelectedItem();
                if (isNull(unit_name)) {
                    unit_name = unit_num + "#";
                }
                String unit_type = (String) jc23.getSelectedItem();
                String value_init_str = jt24.getText();
                if (!isNum(value_init_str)) {
                    errorMessage("水尺常数输入有误");
                    return;
                }
                String frequnce_str = jt27.getText();
                if (!isNum(frequnce_str)) {
                    errorMessage("采集频率输入有误");
                    return;
                }

                float value_init = Float.parseFloat(value_init_str);
                String point_name = (String) jc25.getSelectedItem();
                String place_name = jt26.getText();
                unitBean.setUnit_num((byte) (unit_num & 0xff));
                unitBean.setUnit_name(unit_name);
                unitBean.setSenser_type_name(unit_type);
                unitBean.setValue_init(value_init);
                unitBean.setPoint_name(point_name);
                unitBean.setPlace_name(place_name);
                unitBean.setFrequency(Float.parseFloat(frequnce_str));
                if (unitService.saveUnit(unitBean)) {
                    successMessage("保存成功");
                } else {
                    falseMessage("保存失败");
                }
                dispose();
            }
        });
        return this;
    }


}
