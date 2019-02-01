package com.thingtek.view.shell.systemSetup.systemSetupComptents.base;

import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.config.ClassConfig;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.factory.Factorys;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;


public
abstract class BaseSystemPanel extends JPanel {

    protected Integer authority = UserService.UP;

    public Integer getAuthority() {
        return authority;
    }

    @Resource
    protected Factorys factorys;

    @Resource
    protected UnitService unitService;

    @Resource
    protected PointService pointService;

    @Resource
    protected UserService userService;

    @Resource
    protected ClassConfig classConfig;


    private JPanel centerPanel, toolbar;
    /*protected int x, y, height = 30, yheight = 60, widthlabel = 100, widthinput = 300;*/
    private JLabel titleLabel;

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public BaseSystemPanel() {
        setBackground(Color.WHITE);
    }

    public BaseSystemPanel init() {
        this.setLayout(new BorderLayout());
        initTitle();
        initCenter();
        initToolbar();
        return this;
    }

    protected void initTitle() {
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(factorys.getFontFactory().getFont("systemSetupTitle"));
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));// 绘制空白边框
        this.add(titleLabel, BorderLayout.NORTH);
    }

    protected void initCenter() {
//        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        this.centerPanel.setBorder(factorys.getBorderFactory().getLineBorder("component_Border"));
//        this.centerPanel.setPreferredSize(new Dimension(500, 320));
//        center.add(this.centerPanel);
//        this.add(center, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    protected void initToolbar() {
        toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbar.setOpaque(false);
        toolbar.setBorder(factorys.getBorderFactory().getLineBorder("component_Border"));
        this.add(toolbar, BorderLayout.SOUTH);
    }

    protected EditButton addTool(String text, String icon) {
        EditButton editButton = new EditButton(text, factorys.getIconFactory().getIcon(icon));
        editButton.setToolTipText(text);
        toolbar.add(editButton);
        return editButton;
    }

    protected void addCenter(Component component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        if (component instanceof JComboBox) {
            component.setEnabled(false);
        }
        centerPanel.add(component);
    }

    protected void addCenter(Component component, String place) {
        centerPanel.add(component, place);
    }

    protected void addCenter(Component component) {
        centerPanel.add(component, BorderLayout.CENTER);
    }

    public void checkAuthority(UserBean user) {
        this.init();
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

    protected boolean isNum(String text) {
        return text.matches("^([1-9]\\d*|0)(\\.\\d{1,2})?$");
    }


    /*public void refresh() {}*/

}
