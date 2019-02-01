package com.thingtek.view.component.dialog;

import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.component.dialog.base.BaseDialog;
import com.thingtek.view.component.dialog.base.BaseSetDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDialog extends BaseSetDialog {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserDialog(JFrame jFrame,String titleText, Image icon) {
        super(jFrame,titleText,icon);
    }

    @Override
    public BaseDialog initDialog() {
        super.initDialog();

        xstart = 0;
        ystart = 10;
        final JLabel jlusername = new JLabel("用 户 名", JLabel.CENTER);
        addCenter(jlusername, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jlpassword1 = new JLabel("密    码", JLabel.CENTER);
        addCenter(jlpassword1, xstart, ystart, widthlabel, hcomponent);
        ystart += yheight;
        JLabel jlpassword2 = new JLabel("确认密码", JLabel.CENTER);
        addCenter(jlpassword2, xstart, ystart, widthlabel, hcomponent);

        xstart = 90;
        ystart = 10;
        final JTextField jtfusername = new JTextField();
        addCenter(jtfusername, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;

        final JPasswordField jtfpassword1 = new JPasswordField();
        addCenter(jtfpassword1, xstart, ystart, widthinput, hcomponent);
        ystart += yheight;

        final JPasswordField jtfpassword2 = new JPasswordField();
        addCenter(jtfpassword2, xstart, ystart, widthinput, hcomponent);

        setTotalSize(3);
        buttonSave.setText("添加");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = jtfusername.getText();
                String password1 = new String(jtfpassword1.getPassword());
                String password2 = new String(jtfpassword2.getPassword());
                if (isNull(username) || isNull(password1) || isNull(password2)) {
                    JOptionPane.showMessageDialog(null, "用户名或密码不能为空!", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!password1.equals(password2)) {
                    JOptionPane.showMessageDialog(null, "两次输入的密码不一致!", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (userService.checkUser(jtfusername.getText()) != null) {
                    JOptionPane.showMessageDialog(null, "用户已存在，请勿重复添加!", "失败",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                userBean = new UserBean();
                userBean.setUsername(username);
                userBean.setPassword(password1);
                if (userService.addUser(userBean)) {
                    JOptionPane.showMessageDialog(null, "添加成功!", "成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "添加失败!请稍后重试...", "失败",
                            JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });
        return this;
    }

    private UserBean userBean;

}
