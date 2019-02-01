package com.thingtek.view.login;


import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.Shell;
import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.component.panel.ShadowPanel;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@EqualsAndHashCode(callSuper = true)
@org.springframework.stereotype.Component
public @Data
class Login extends JFrame {

    @Resource
    private Factorys factorys;
    @Resource
    private Shell shell;
    @Resource
    private LogoInfo logoInfo;

    private JPanel jpBackground, jpLoad, jpLogin;
    private JLabel jlbPageInfo;
    private JProgressBar jpbProgress;
    private JLabel jlbProgress;
    // 登录、取消按钮
    private JButton jbtApply;
    // 用户输入区域
    private JTextField jtfUserName;
    // 密码框
    private JPasswordField jpfPSW;

    // 构造方法
    public Login() {
    }

    public Login init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        JPanel contentPane = new JPanel(new BorderLayout());
//        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        Image image = factorys.getIconFactory().getImage("load");
        jpBackground = new ShadowPanel(image, 1.0f);
        jpBackground.setLayout(null);
        contentPane.add(jpBackground, BorderLayout.CENTER);


        JLabel jlbSoftname = new JLabel(logoInfo.getSoftName(), JLabel.CENTER);
        jlbSoftname.setFont(factorys.getFontFactory().getFont("loginSoftnameFont"));
        jlbSoftname.setForeground(factorys.getColorFactory().getColor("loginSoftnameFore"));
        addBackGround(jlbSoftname, 10, 10, 380, 40);

        jlbPageInfo = new JLabel("用户登录");
        jlbPageInfo.setFont(factorys.getFontFactory().getFont("loginPageInfoFont"));
        jlbPageInfo.setForeground(factorys.getColorFactory().getColor("loginPageInfoFore"));
        addBackGround(jlbPageInfo, 40, 75, 120, 22);

        JLabel jlbCopyright = new JLabel(logoInfo.getCopyrightName() + logoInfo.getCompanyName(), JLabel.CENTER);
        jlbCopyright.setFont(factorys.getFontFactory().getFont("loginCopyrightFont"));
        jlbCopyright.setForeground(factorys.getColorFactory().getColor("loginCopyrightFore"));
        addBackGround(jlbCopyright, 10, 250, 380, 20);

        this.setIconImages(factorys.getIconFactory().getLogoIconTexts());
        this.setTitle(logoInfo.getSoftName());
        // 去除JDialog边框
//        this.setUndecorated(true);
        // 透明
        // AWTUtilities.setWindowOpaque(this, false);
        // 设置JDialog尺寸为背景图片大小
        this.setSize(400, 300);
        // 设置窗体居中
        this.setLocationRelativeTo(null);
        this.setContentPane(contentPane);
        this.initLogin();
        this.initLoad();
        this.setVisible(true);
        return this;
    }

    private void initLoad() {
        jpLoad = new JPanel(null);
        jpLoad.setVisible(false);
        addBackGround(jpLoad, 80, 120, 240, 120);

//        JLabel jlbBridge = new JLabel(new ImageIcon(this.getClass().getResource("bridge.png")));
        JLabel jlbBridge = new JLabel();
        addLoad(jlbBridge, 0, 0, 240, 80);

        jlbProgress = new JLabel();
        jlbProgress.setHorizontalAlignment(JLabel.CENTER);// 文字居中
        addLoad(jlbProgress, 0, 70, 240, 18);

        jpbProgress = new JProgressBar(0, 100);
        jpbProgress.setStringPainted(true);// 显示百分比字符
        jpbProgress.setIndeterminate(false); // 不确定的进度条
        jpbProgress.setBorderPainted(false);// 取消边框
        jpbProgress.setOpaque(false);// 设置透明背景
        addLoad(jpbProgress, 0, 90, 240, 12);
    }

    private void initLogin() {
        jpLogin = new JPanel(null);
        addBackGround(jpLogin, 80, 120, 240, 120);

        JLabel jlbName = new JLabel("用户名:", JLabel.RIGHT);
        addLogin(jlbName, 0, 5, 75, 20);

        JLabel jlbPSW = new JLabel("密    码:", JLabel.RIGHT);
        addLogin(jlbPSW, 0, 35, 75, 20);

        jtfUserName = new JTextField();
        addLogin(jtfUserName, 80, 5, 160, 20);

        jpfPSW = new JPasswordField();
        jpfPSW.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jbtApply.doClick();
                }
            }
        });
        addLogin(jpfPSW, 80, 35, 160, 20);

        jbtApply = new JButton("确定", factorys.getIconFactory().getIcon("apply"));
        jbtApply.setOpaque(false);
        jbtApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkInput()) {
                    JOptionPane.showMessageDialog(null, "用户名/密码不能为空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                UserBean userBean = getUser();
                UserBean user = userService.checkUser(userBean);
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "用户不存在/密码错误", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                toloading();
                load();

                   /* switch (user.getAuthority()) {
                        case UserService.DP:
                            break;
                        case UserService.AP:
                            break;
                        case UserService.UP:
                            break;
                    }*/


            }
        });
        addLogin(jbtApply, 20, 70, 100, 30);

        JButton jbtCancel = new JButton("取消", factorys.getIconFactory().getIcon("cancel"));
        jbtCancel.setOpaque(false);
        addLogin(jbtCancel, 130, 70, 100, 30);
        jbtCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // 加载进度显示
    private void loading(int percent, String loading) {
        jpbProgress.setValue(percent);
        jlbProgress.setText(loading);
    }

    private void load() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                shell.init();
                Map<String, BasePanel> basemap = logoInfo.getBasePanelMap();
                Map<String, Map<String, BaseSystemPanel>> sysmap = logoInfo.getSetPanelMap();
                int onepercent = 100 / (basemap.size() + sysmap.size());
                int percent = 0;
                Set<Map.Entry<String, BasePanel>> basemapEntries = basemap.entrySet();
                for (Map.Entry<String, BasePanel> entry : basemapEntries) {
                    loading(percent, "Loading " + entry.getKey());
                    percent += onepercent;
                    shell.addToolItem(entry.getValue(), entry.getKey());
                }
                Set<Map.Entry<String, Map<String, BaseSystemPanel>>> sysmapEntries = sysmap.entrySet();
                for (Map.Entry<String, Map<String, BaseSystemPanel>> entry : sysmapEntries) {
                    loading(percent, "Loading " + entry.getKey());
                    percent += onepercent;
                    shell.addSystemSetMenuItem(entry.getValue(), entry.getKey());
                }
                dispose();
                shell.setVisible(true);
            }
        };
        timer.schedule(task, 500);
    }

    private void toloading() {
        jlbPageInfo.setText("正在加载...");
        jpLoad.setVisible(true);
        jpLogin.setVisible(false);
        jpBackground.validate();
        jpBackground.updateUI();
    }


    private boolean checkInput() {
        String userName = jtfUserName.getText().trim().toLowerCase();
        String password = new String(jpfPSW.getPassword()).toLowerCase();
        return userName.equals("") || password.equals("");
    }

    private UserBean getUser() {
        String userName = jtfUserName.getText().trim().toLowerCase();
        String password = new String(jpfPSW.getPassword()).toLowerCase();
        UserBean userBean = new UserBean();
        userBean.setUsername(userName);
        userBean.setPassword(password);
        return userBean;
    }


    private void addBackGround(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setOpaque(false);
        jpBackground.add(component);
    }

    private void addLogin(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setForeground(factorys.getColorFactory().getColor("loginlogfore"));
        component.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        jpLogin.add(component);
    }

    private void addLoad(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setForeground(factorys.getColorFactory().getColor("loginloadfore"));
        jpLoad.add(component);
    }

    @Resource
    private UserService userService;

}
