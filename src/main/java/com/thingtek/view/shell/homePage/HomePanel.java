package com.thingtek.view.shell.homePage;


import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.logo.LogoInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@EqualsAndHashCode(callSuper = true)
public @Data
class HomePanel extends GlassPanel {

    private HomePanel() {
        authority = UserService.UP;
    }

    private String logoinfo;

    public HomePanel init() {
        super.init();
        this.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setOpaque(false);

        this.add(center, BorderLayout.CENTER);

        JPanel bottom = new GlassPanel(new BorderLayout(), 0.3f);

        JLabel companyName = new JLabel(logoinfo, JLabel.CENTER);
        companyName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bottom.add(companyName, BorderLayout.NORTH);


//        this.add(bottom, BorderLayout.SOUTH);
        return this;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}