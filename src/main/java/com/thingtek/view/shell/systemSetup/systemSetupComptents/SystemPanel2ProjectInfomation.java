package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.proinfo.entity.ProInfoBean;
import com.thingtek.beanServiceDao.proinfo.service.ProInfoService;
import com.thingtek.serialPort.SerialTool;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

@Component
public class SystemPanel2ProjectInfomation extends BaseSystemPanel {

    @Resource
    private Shell shell;
    @Resource
    private LogoInfo logoInfo;
    @Resource
    private SerialTool serialTool;

    @Resource
    private ProInfoService proInfoService;

    private JTextField jtfProName, jtfVertical, jtfHorizontal, jtfManager, jtfTelNum;
    private JComboBox<String> jcbComs;

    @Override
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
        JLabel jlR31 = new JLabel("试验名称:", JLabel.RIGHT);
        jlR31.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR31, gbc);
        gblJpanel.add(jlR31);
        gbc.gridy++;
        JLabel jlR32 = new JLabel("垂直比尺:", JLabel.RIGHT);
        jlR32.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR32, gbc);
        gblJpanel.add(jlR32);
        gbc.gridy++;
        JLabel jlR33 = new JLabel("水平比尺:", JLabel.RIGHT);
        jlR33.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR33, gbc);
        gblJpanel.add(jlR33);
        gbc.gridy++;
        JLabel jlR34 = new JLabel("负 责 人:", JLabel.RIGHT);
        jlR34.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR34, gbc);
        gblJpanel.add(jlR34);
        gbc.gridy++;
        JLabel jlR35 = new JLabel("联系电话:", JLabel.RIGHT);
        jlR35.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR35, gbc);
        gblJpanel.add(jlR35);
        gbc.gridy++;
        JLabel jlR36 = new JLabel("串口选择:", JLabel.RIGHT);
        jlR36.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        gbl.setConstraints(jlR36, gbc);
        gblJpanel.add(jlR36);


        Dimension dimension = new Dimension(300, 25);
        gbc.gridx++;
        gbc.gridy = 0;
        gbc.weightx = 2;
        jtfProName = new JTextField();
        jtfProName.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jtfProName.setPreferredSize(dimension);
        gbl.setConstraints(jtfProName, gbc);
        gblJpanel.add(jtfProName);
        gbc.gridy++;
        jtfVertical = new JTextField();
        jtfVertical.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jtfVertical.setPreferredSize(dimension);
        gbl.setConstraints(jtfVertical, gbc);
        gblJpanel.add(jtfVertical);
        gbc.gridy++;
        jtfHorizontal = new JTextField();
        jtfHorizontal.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jtfHorizontal.setPreferredSize(dimension);
        gbl.setConstraints(jtfHorizontal, gbc);
        gblJpanel.add(jtfHorizontal);
        gbc.gridy++;
        jtfManager = new JTextField();
        jtfManager.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jtfManager.setPreferredSize(dimension);
        gbl.setConstraints(jtfManager, gbc);
        gblJpanel.add(jtfManager);
        gbc.gridy++;
        jtfTelNum = new JTextField();
        jtfTelNum.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        jtfTelNum.setPreferredSize(dimension);
        gbl.setConstraints(jtfTelNum, gbc);
        gblJpanel.add(jtfTelNum);
        gbc.gridy++;
        Vector<String> coms = serialTool.findPort();
        jcbComs = new JComboBox<>(coms);
        jcbComs.setPreferredSize(dimension);
        gbl.setConstraints(jcbComs, gbc);
        gblJpanel.add(jcbComs);

        ProInfoBean info = proInfoService.getInfo();
        if (info != null) {
            jtfProName.setText(info.getPro_name());
            jtfVertical.setText(String.valueOf(info.getVertical_value()));
            jtfHorizontal.setText(String.valueOf(info.getHorizontal_value()));
            jtfManager.setText(info.getManager());
            jtfTelNum.setText(info.getTel_num());
            if (coms.contains(info.getCom())) {
                jcbComs.setSelectedItem(info.getCom());
            } else {
                info.setCom((String) jcbComs.getSelectedItem());
            }
            setShell(info);
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        EditButton save = addTool("保存", "apply");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vertical = jtfVertical.getText();
                String horizontal = jtfHorizontal.getText();
                if (!isNum(vertical) || !isNum(horizontal)) {
                    errorMessage("比尺输入有误");
                    return;
                }
                String tel = jtfTelNum.getText();
                if (!isTelNum(tel)) {
                    errorMessage("联系方式输入有误");
                    return;
                }
                ProInfoBean info = getProInfo();
                if (proInfoService.save(info)) {
                    shell.setTitle(logoInfo.getSoftName() + "--" + info.getPro_name());
                    successMessage("保存成功");
                    setShell(info);
                } else {
                    falseMessage("保存失败");
                }
            }
        });
        EditButton refresh = addTool("串口刷新", "refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jcbComs.removeAllItems();
                for (String com : serialTool.findPort()) {
                    jcbComs.addItem(com);
                }
            }
        });

    }

    private void setShell(ProInfoBean info) {
        shell.setTitle(logoInfo.getSoftName() + "--" + info.getPro_name());
        shell.setVertical(String.valueOf(info.getVertical_value()));
        shell.setHorizontalLabel(String.valueOf(info.getHorizontal_value()));
        shell.setManagerInfo(info.getManager());
    }

    private ProInfoBean getProInfo() {
        ProInfoBean proinfo = new ProInfoBean();
        proinfo.setPro_name(jtfProName.getText());
        proinfo.setVertical_value(Double.parseDouble(jtfVertical.getText()));
        proinfo.setHorizontal_value(Double.parseDouble(jtfHorizontal.getText()));
        proinfo.setManager(jtfManager.getText());
        proinfo.setTel_num(jtfTelNum.getText());
        proinfo.setCom((String) jcbComs.getSelectedItem());
        return proinfo;
    }

    /*protected boolean isNum(String text) {
        String reg = "\\d+";
        return text.matches(reg);
    }*/

    private boolean isTelNum(String text) {
        String reg = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$";
        return text.matches(reg);
    }

}
