package com.thingtek.view.shell.systemSetup.systemSetupComptents;


import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.component.dialog.UserDialog;
import com.thingtek.view.component.label.UserLabel;
import com.thingtek.view.component.layout.ModifiedFlowLayout;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * 用户管理
 */
@Component
public class SystemPanel2User extends BaseSystemPanel {

    private UserLabel tempLabel;

    private JPanel center;

    @Resource
    private Shell shell;

    public SystemPanel2User() {
        authority = UserService.AP;
    }

    /*@Override
    protected void initCenter() {
        super.initCenter();
        JLabel jlabel = new JLabel("请选择要更改的用户", JLabel.LEFT);
        addCenter(jlabel, 0, 0, 500, 31);
        center = new JPanel(new ModifiedFlowLayout(FlowLayout.LEFT, 20, 20));
        JScrollPane scrollPane = new JScrollPane(center);
        addCenter(scrollPane, 0, 30, 500, 290);
    }*/
    protected void initCenter() {
        super.initCenter();
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setOpaque(false);
        JLabel jlabel = new JLabel("请选择要更改的用户", JLabel.LEFT);
        jPanel.add(jlabel, BorderLayout.NORTH);
        center = new JPanel(new ModifiedFlowLayout(FlowLayout.LEFT, 2, 2));
        center.setOpaque(false);
        final JScrollPane scrollPane = new JScrollPane(center);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        jPanel.add(scrollPane, BorderLayout.CENTER);
        /*scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
//                System.out.println("scrollpane"+scrollPane.getSize());
//                System.out.println("viewport:"+scrollPane.getViewport().getSize());
                center.setPreferredSize(scrollPane.getViewport().getSize());
                center.validate();
                center.repaint();
                center.updateUI();
            }
        });*/
        addCenter(jPanel);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        //添加用户:展开"添加用户"面板,执行添加操作
        JButton addUser = addTool("添加", "add");
        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDialog dialog = new UserDialog(shell, "添加用户", factorys.getIconFactory().getImage("user"));
                dialog.setFactorys(factorys);
                dialog.setUserService(userService);
                dialog.initDialog().visible();
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshUsers();
                    }
                });
            }
        });

        //更改用户类型:修改用户权限，对用户进行管理员与普通用户的切换
        JButton changeUser = addTool("权限更改", "change");
        changeUser.setToolTipText("更改用户权限");
        changeUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSelectUser();
            }
        });

        //删除用户:将用户删除
        JButton deleteUser = addTool("删除", "delete");
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectUser();
            }
        });

        // 刷新用户列表:刷新系统内所有用户，刷新用户列表
        JButton refreshUser = addTool("刷新", "refresh");
        refreshUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUsers();
            }
        });
        refreshUsers();
    }

    /**
     * 获取选中用户
     */
    private void changeSelectUser() {
        if (tempLabel == null) {
            errorMessage("请选择操作的用户");
            return;
        }
        if (userService.changeRole(tempLabel.chageAuthority())) {
            tempLabel.repaint();
            tempLabel.updateUI();
            successMessage("更改成功");
        } else {
            falseMessage("更改失败,请稍后重试");
        }
    }

    private void deleteSelectUser() {
        if (tempLabel == null) {
            errorMessage("请选择操作的用户");
            return;
        }
        if (userService.deleteUser(tempLabel.getUser())) {
            successMessage("删除成功");
        } else {
            falseMessage("删除失败,请稍后重试");
        }
        refreshUsers();
    }


    /**
     * 加载、刷新用户列表
     */
    private void refreshUsers() {
        List<UserBean> users = userService.query();
        tempLabel = null;
        if (center.getComponentCount() > 0) {
            center.removeAll();
        }

        for (UserBean user : users) {
            UserLabel temp = new UserLabel(user);
            temp.setFactorys(factorys);
            temp.init();
            temp.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (tempLabel != null) {
                        tempLabel.setOpaque(false);
                        tempLabel.setBorder(null);
                        tempLabel.repaint();
                        tempLabel = null;
                    }
                    tempLabel = (UserLabel) e.getComponent();
                    tempLabel.setOpaque(true);
                    tempLabel.setBorder(factorys.getBorderFactory().getLineBorder("userTempBorder"));
                    tempLabel.repaint();
                }
            });
            center.add(temp);
        }
        users.clear();
        center.repaint();
        center.revalidate();
    }

    private boolean isNull(String s) {
        return s == null || s.equals("");
    }


    @Override
    public void checkAuthority(UserBean user) {
        super.checkAuthority(user);
    }

}
