package com.thingtek.view.component.dialog.base;

import com.thingtek.beanServiceDao.unit.service.UnitService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public
abstract class BaseSetDialog extends BaseDialog {

    protected UnitService unitService;

    protected int xstart, ystart, hcomponent = 20, yheight = 30, widthlabel = 80, widthinput = 130;

    public BaseSetDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }


    @Override
    public BaseDialog initDialog() {
        return super.initDialog();
    }

    private JPanel centerPanel;

    @Override
    public void initCenter() {
        centerPanel = new JPanel(null);
        container.add(centerPanel, BorderLayout.CENTER);

    }

    protected void addCenter(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        centerPanel.add(component);
    }

    @Override
    public void initTool() {
        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        bottomPane.setBackground(new Color(240, 240, 240));
        container.add(bottomPane, BorderLayout.SOUTH);

        buttonSave = new JButton(factorys.getIconFactory().getIcon("apply"));
        buttonSave.setPreferredSize(new Dimension(100, 28));
        bottomPane.add(buttonSave);

        JButton buttonCancel = new JButton("取消", factorys.getIconFactory().getIcon("cancel"));
        buttonCancel.setToolTipText("取消");
        buttonCancel.setPreferredSize(new Dimension(100, 28));
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPane.add(buttonCancel);
    }


    @Override
    public int getWidth() {
        return 300;
    }


    protected void setTotalSize(int ycount) {
        setSize(240, 76 + ycount * 30);
    }

    protected boolean isNull(String string) {
        return string == null || string.trim().equals("");
    }


    protected void errorMessage(String text) {
        JOptionPane.showMessageDialog(this, text, "错误", JOptionPane.ERROR_MESSAGE);
    }

    protected void falseMessage(String text) {
        JOptionPane.showMessageDialog(this, text, "失败", JOptionPane.ERROR_MESSAGE);
    }

    protected void successMessage(String text) {
        JOptionPane.showMessageDialog(this, text, "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    protected boolean isNum(String str) {
        return str.matches("^([1-9]\\d*|0)(\\.\\d{1,2})?$");
    }
}
