package com.thingtek.view.component.label;

import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.component.factory.Factorys;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@EqualsAndHashCode(callSuper = true)
public @Data
class UserLabel extends JLabel {

    private UserBean user;
    private Factorys factorys;

    public UserLabel(UserBean user) {
        this.user = user;
    }

    public void init() {
        setBackground(factorys.getColorFactory().getColor("userTemp"));
        setText(user.getUsername());
        switch (user.getAuthority()) {
            case UserService.DP:
                setToolTipText(UserService.DEVELOP);
                setIcon(factorys.getIconFactory().getIcon("develop"));
                break;
            case UserService.AP:
                setToolTipText(UserService.ADMIN);
                setIcon(factorys.getIconFactory().getIcon("admin"));
                break;
            case UserService.UP:
                setToolTipText(UserService.USER);
                setIcon(factorys.getIconFactory().getIcon("user"));
                break;
        }
        setBackground(factorys.getColorFactory().getColor("userTemp"));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                arg0.getComponent().requestFocus();
            }
        });
    }

    public UserBean chageAuthority() {
        switch (user.getAuthority()) {
            case UserService.AP:
                user.setAuthority(UserService.UP);
                setIcon(factorys.getIconFactory().getIcon("user"));
                break;
            case UserService.UP:
                user.setAuthority(UserService.AP);
                setIcon(factorys.getIconFactory().getIcon("admin"));
                break;
        }
        return user;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }

    @Override
    public int getVerticalTextPosition() {
        return SwingConstants.BOTTOM;
    }

    @Override
    public int getVerticalAlignment() {
        return SwingConstants.CENTER;
    }

    @Override
    public int getHorizontalTextPosition() {
        return SwingConstants.CENTER;
    }

    @Override
    public int getHorizontalAlignment() {
        return SwingConstants.CENTER;
    }

    @Override
    public boolean isFocusable() {
        return true;
    }
}
