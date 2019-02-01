package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 修改密码 子面板
 */
@Component

public class SystemPanel2PassWord extends BaseSystemPanel {

    // 原密码，新密码
    private JPasswordField jpfR31, jpfR32, jpfR33;

    public SystemPanel2PassWord() {
        authority = UserService.UP;
    }


    protected void initToolbar() {
        super.initToolbar();
        JButton edit = addTool("修改", "set");
        JButton cancel = addTool("退出", "cancel");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPassword();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });
    }

    @Override
    public void checkAuthority(UserBean user) {
        super.checkAuthority(user);
    }


    /*protected void initCenter2() {
        super.initCenter();
        x = 40;
        y = 80;


        JLabel jlR31 = new JLabel("当前密码:", JLabel.LEFT);
        addCenter(jlR31, x, y, widthlabel, height);
        y += yheight;
        JLabel jlR32 = new JLabel("新密码:", JLabel.LEFT);
        addCenter(jlR32, x, y, widthlabel, height);
        y += yheight;
        JLabel jlR33 = new JLabel("确认密码:", JLabel.LEFT);
        addCenter(jlR33, x, y, widthlabel, height);
        x = 160;
        y = 80;
        // 当前密码输入
        jpfR31 = new JPasswordField();
        addCenter(jpfR31, x, y, widthinput, height);
        y += yheight;

        // 新密码输入
        jpfR32 = new JPasswordField();
        addCenter(jpfR32, x, y, widthinput, height);
        y += yheight;
        // 新密码输入
        jpfR33 = new JPasswordField();
        addCenter(jpfR33, x, y, widthinput, height);
    }*/

    protected void initCenter() {
        super.initCenter();

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel gblJpanel = new JPanel(gbl);
        gblJpanel.setOpaque(false);

        JPanel panel = new JPanel();
        panel.setOpaque(false);

        gblJpanel.setPreferredSize(new Dimension(500, 200));

        panel.add(gblJpanel);

        addCenter(panel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JLabel jlR31 = new JLabel("当前密码:", JLabel.LEFT);
        jlR31.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR31, gbc);
        gblJpanel.add(jlR31);

        gbc.gridy++;

        JLabel jlR32 = new JLabel("新密码:", JLabel.LEFT);
        jlR32.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR32, gbc);
        gblJpanel.add(jlR32);

        gbc.gridy++;

        JLabel jlR33 = new JLabel("确认密码:", JLabel.LEFT);
        jlR33.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR33, gbc);
        gblJpanel.add(jlR33);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.weightx = 2;

        Dimension dimension = new Dimension(300, 25);

        jpfR31 = new JPasswordField();
        jpfR31.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jpfR31.setPreferredSize(dimension);
        gbl.setConstraints(jpfR31, gbc);
        gblJpanel.add(jpfR31);

        gbc.gridy++;

        jpfR32 = new JPasswordField();
        jpfR32.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jpfR32.setPreferredSize(dimension);
        gbl.setConstraints(jpfR32, gbc);
        gblJpanel.add(jpfR32);

        gbc.gridy++;

        jpfR33 = new JPasswordField();
        jpfR33.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jpfR33.setPreferredSize(dimension);
        gbl.setConstraints(jpfR33, gbc);
        gblJpanel.add(jpfR33);

    }


    private void cancelEdit() {
        jpfR31.setText(null);
        jpfR32.setText(null);
        jpfR33.setText(null);
    }


    private void checkPassword() {
        String password1 = new String(jpfR31.getPassword())
                .toLowerCase();
        String password2 = new String(jpfR32.getPassword())
                .toLowerCase();
        String password3 = new String(jpfR33.getPassword())
                .toLowerCase();
        // 做个密码框是不是都有输入
        if (isNull(password1) || isNull(password2) || isNull(password3)) {
            errorMessage("密码不能为空");
            return;
        }
        // 判断新密码是否与当前密码一致
        if (password2.equals(password1)) {
            errorMessage("新密码不能与当前密码一致");
            return;
        }
        // 判断两次新密码是否一致
        if (!password3.equals(password2)) {
            errorMessage("两次新密码不一致");
            return;
        }
        // 判断当前密码是否正确
        if (!userService.checkPassWord(password1)) {
            errorMessage("当前密码错误");
            return;
        }

        if (userService.changePassword(password2)) {
            successMessage("密码修改成功");
        } else {
            falseMessage("密码修改失败");
        }
        jpfR31.setText(null);
        jpfR32.setText(null);
        jpfR33.setText(null);
    }

    private boolean isNull(String s) {
        return s == null || s.equals("");
    }
}
