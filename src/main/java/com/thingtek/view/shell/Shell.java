package com.thingtek.view.shell;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.serialPort.SerialConnect;
import com.thingtek.view.component.button.ShellTitleButton;
import com.thingtek.view.component.dialog.SystemDialog;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.component.panel.BackGroundPanel;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.dataCollect.Collect;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

@org.springframework.stereotype.Component
public class Shell extends JFrame {

    @Resource
    private LogoInfo logoInfo;

    @Resource
    private UserService userService;

    @Resource
    private Factorys factorys;

    // 虚线框
//    private DashedBorder dashedBorder;
//    private Point lastPoint;
//    private boolean isMaximized = false;
//    private JPopupMenu pop;
//    private JButton btnMax;
//    private boolean isDragged = false;
    private JPanel normalpanel;
    private JPanel toolBar;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private CardLayout centerCard;// 卡片布局

    public Dimension dimension = new Dimension(1000, 600);

    public Shell init() {
        collectlist = new ArrayList<>();
        this.initWindow();
        this.initMenu();
        this.initTop();
        this.initCenter();
        this.initBottom();
        return this;
    }

    private void initWindow() {

        this.setIconImages(factorys.getIconFactory().getLogoIconTexts());
        this.setTitle(logoInfo.getSoftName());// 标题

//        this.setUndecorated(true);// 去除边框修饰
//        AWTUtilities.setWindowOpaque(this, false);// 设置透明
        this.setSize(dimension);
        this.setMinimumSize(new Dimension(800,600));
        this.setLocationRelativeTo(null);
        CardLayout contentCard = new CardLayout();
        JPanel contentPane = new JPanel(contentCard);
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        setContentPane(contentPane);

        normalpanel = new BackGroundPanel(factorys.getIconFactory().getImage("databackground"));

        contentPane.add(normalpanel, "normal");

//        dashedBorder = new DashedBorder();
//        dashedBorder.setBounds(this.getBounds());
        // 窗体关闭弹出对话框提示："确定"、"取消"
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(Shell.this, "确认关闭?", "关闭程序", JOptionPane.OK_CANCEL_OPTION);
                switch (option) {
                    case JOptionPane.OK_OPTION:
                        exitSys();
                        break;
                }
            }
        });

    }

    private java.util.List<Collect> collectlist;
    @Resource
    private SerialConnect connect;
    private JMenuBar menuBar;

    private void initMenu() {
        menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        this.setJMenuBar(menuBar);
    }

    private void initTop() {
        // 顶部面板：放置标题面板和功能面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        normalpanel.add(topPanel, BorderLayout.NORTH);
        /*topPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnMax.doClick();
                }
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                if (!isMaximized && isShowing()) {
                    lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
                    dashedBorder.setVisible(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isMaximized & isDragged) {
                    isDragged = false;
                    Shell.this.setLocation(dashedBorder.getLocation());
                }
                dashedBorder.setVisible(false);
            }
        });
        topPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isMaximized && isShowing()) {
                    isDragged = true;
                    Point location = dashedBorder.getLocation();
                    Point tempPonit = e.getLocationOnScreen();
                    dashedBorder.setLocation(location.x + tempPonit.x - lastPoint.x,
                            location.y + tempPonit.y - lastPoint.y);
                    lastPoint = tempPonit;
                }
            }
        });

        // 标题面板：放置logo和窗口工具
        JPanel tiltlePanel = new JPanel(new BorderLayout());
        tiltlePanel.setOpaque(false);
        topPanel.add(tiltlePanel, BorderLayout.NORTH);

        JLabel log = new JLabel(" " + logoInfo.getSoftName());
        log.setForeground(Color.BLACK);
        tiltlePanel.add(log, BorderLayout.WEST);

        // 窗口操作面板，“最小化”、“最大化”、关闭
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        tiltlePanel.add(right, BorderLayout.EAST);

        initSetPop();

        JButton btnSet = new JButton(new SetIcon());
        btnSet.setToolTipText("主菜单");
        btnSet.setFocusable(false);
        // 无边框
        btnSet.setBorder(null);
        // 取消绘制按钮内容区域
        btnSet.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnSet.setFocusPainted(false);
        btnSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton jb = (JButton) e.getSource();
                pop.show(jb, 0, jb.getHeight());
            }
        });
        right.add(btnSet);

        JButton btnMin = new JButton(new MinIcon());
        btnMin.setToolTipText("最小化");
        btnMin.setFocusable(false);
        // 无边框
        btnMin.setBorder(null);
        // 取消绘制按钮内容区域
        btnMin.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnMin.setFocusPainted(false);
        right.add(btnMin);
        btnMin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shell.this.setExtendedState(JFrame.ICONIFIED);
                // 此处只能是Frame.setStatr(state),否则在最大化模式下最小化后，
                // 再点击状态栏图标就不能还原最大化,只能显示JFrame.NORMAL状态
                Shell.this.setState(Frame.ICONIFIED);
            }
        });

        btnMax = new JButton(new MaxIcon());
        btnMax.setToolTipText("最大化");
        btnMax.setFocusable(false);
        // 无边框
        btnMax.setBorder(null);
        // 取消绘制按钮内容区域
        btnMax.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnMax.setFocusPainted(false);
        right.add(btnMax);
        btnMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMaximized) {
                    Shell.this.setExtendedState(Frame.NORMAL);
                    Shell.this.setBounds(dashedBorder.getBounds());
                    btnMax.setToolTipText("最大化");
                } else {
                    Shell.this.setExtendedState(Frame.MAXIMIZED_BOTH);
                    Shell.this.setBounds(getMaxBounds());
                    btnMax.setToolTipText("向下还原");
                }
                isMaximized = !isMaximized;
                btnMax.setSelected(isMaximized);
            }
        });

        JButton btnClose = new JButton(new CloseIcon());
        btnClose.setToolTipText("关闭");
        btnClose.setFocusable(false);
        // 无边框
        btnClose.setBorder(null);
        // 取消绘制按钮内容区域
        btnClose.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnClose.setFocusPainted(false);
        right.add(btnClose);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                exitSys();
            }
        });*/

        // 功能面板，放置“主界面”、“数据采集”······
        JPanel funcionPanel = new JPanel(new BorderLayout());
        funcionPanel.setOpaque(false);
        topPanel.add(funcionPanel, BorderLayout.CENTER);
        toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        toolBar.setOpaque(false);
        funcionPanel.add(toolBar, BorderLayout.CENTER);

    }


    /*private void initSetPop() {

        pop = new JPopupMenu();
        JCheckBoxMenuItem voiceAlarm = new JCheckBoxMenuItem("声音报警",
                factorys.getIconFactory().getIcon("sound"));
        voiceAlarm.setSelected(VoiceConfig.isVioceWarn());
        voiceAlarm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VoiceConfig.setVoiceWarn(((JCheckBoxMenuItem) e.getSource())
                        .getState());
            }
        });
        pop.add(voiceAlarm);


    }*/

    /*private Rectangle getMaxBounds() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screenSize);
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        return bounds;
    }*/

    // 初始化中间面板
    private void initCenter() {
        centerCard = new CardLayout();
        centerPanel = new JPanel(centerCard);
        centerPanel.setOpaque(false);
        normalpanel.add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel verticalLabel;
    private JLabel horizontalLabel;
    private JLabel managerLabel;

    public void setVertical(String vertical) {
        verticalLabel.setText(vertical);
    }

    public void setHorizontalLabel(String horizontal) {
        horizontalLabel.setText(horizontal);
    }

    public void setManagerInfo(String managerInfo) {
        managerLabel.setText(managerInfo);
    }

    private void initBottom() {
        bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setOpaque(false);
        verticalLabel = new JLabel("1");
        verticalLabel.setOpaque(false);
        horizontalLabel = new JLabel("1");
        horizontalLabel.setOpaque(false);
        managerLabel = new JLabel("***");
        horizontalLabel.setOpaque(false);

        JLabel companyName = new JLabel("  " + logoInfo.getCopyrightName() + logoInfo.getCompanyName(), JLabel.CENTER);
        companyName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        companyName.setOpaque(false);
        bottomPanel.add(companyName, BorderLayout.WEST);

        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        bottomPanel.add(jPanel, BorderLayout.EAST);

        jPanel.add(new JLabel("垂直比尺: 1 :"));
        jPanel.add(verticalLabel);
        jPanel.add(new JLabel("水平比尺: 1 :"));
        jPanel.add(horizontalLabel);
        jPanel.add(new JLabel("管理员:"));
        jPanel.add(managerLabel);
        normalpanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 退出程序
     */
    private void exitSys() {
//        setVisible(false);
        if (connect.isConnectflag()) {
            for (Collect collet : collectlist) {
                collet.stopCollect();
            }
        }
        connect.closeConnect();
        System.exit(0);
    }

    private java.util.List<JButton> titleButtons = new ArrayList<>();

//    private Map<String, BaseMenuDialog> dialogMap = new HashMap<>();

    public void addSystemSetMenuItem(Map<String, BaseSystemPanel> compMap, final String text) {
        JMenu menu = new JMenu(text);
        menuBar.add(menu);
        final SystemDialog systemDialog = new SystemDialog(this, text, factorys.getIconFactory().getImage(text));
        systemDialog.setSize(dimension);
//        dialogMap.put(text, systemDialog);
        Set<Map.Entry<String, BaseSystemPanel>> entries = compMap.entrySet();
        for (final Map.Entry<String, BaseSystemPanel> entry : entries) {
            final String title = entry.getKey();
            JMenuItem item = new JMenuItem(entry.getKey());
            BaseSystemPanel panel = entry.getValue().init();
            panel.setTitle(title);
            menu.add(item);
            systemDialog.addItem(panel, title);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    systemDialog.showItem(title);
                }
            });
        }
    }


    public void addToolItem(final BasePanel component, final String text) {
        if (component.getAuthority() < userService.getUser().getAuthority()) return;
        JButton title = new ShellTitleButton(text, factorys.getIconFactory().getIcon(text));
        title.setFont(factorys.getFontFactory().getFont("title"));
        title.setForeground(factorys.getColorFactory().getColor("titleButtonForeground"));
        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton b : titleButtons) {
                    if (e.getSource() != b) {
                        b.setSelected(false);
                    } else {
                        b.setSelected(true);
                        centerCard.show(centerPanel, text);
                    }
                }
            }
        });
        component.manager(userService.getUser());
        titleButtons.add(title);
        // 内容
        toolBar.add(title);
        centerPanel.add(component, text);
        if (component instanceof Collect) {
            collectlist.add((Collect) component);
        }
    }

}
