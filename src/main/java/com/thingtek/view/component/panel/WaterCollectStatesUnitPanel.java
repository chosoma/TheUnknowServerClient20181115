package com.thingtek.view.component.panel;

import com.thingtek.beanServiceDao.data.entity.WaterDataBean;
import com.thingtek.beanServiceDao.unit.entity.WaterUnitBean;
import org.springframework.web.context.request.FacesRequestAttributes;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WaterCollectStatesUnitPanel extends JPanel {

    private Color cfalse = new Color(200, 50, 50);
    private Color cwarn = new Color(220, 220, 0);
    private Color csuccess = new Color(50, 200, 50);

    private WaterUnitBean unit;

    public WaterCollectStatesUnitPanel(WaterUnitBean unit) {
        this.unit = unit;
        setOpaque(false);
    }

    private JLabel[] labels;
//    private JLabel jlUnit_num, jlUnit_name, jlDate, jlTestValue, jlWaterValue, jlConnectStates, jlWaterInit;

    public WaterCollectStatesUnitPanel init() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        labels = new JLabel[7];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            labels[i].setPreferredSize(new Dimension(120, 20));
            labels[i].setOpaque(true);

            this.add(labels[i]);
            switch (i) {
                case 0:
                    labels[i].setText(String.valueOf(unit.getUnit_num()));
                    break;
                case 1:
                    labels[i].setText(unit.getUnit_name());
                    break;
                case 5:
                    labels[i].setBackground(cfalse);
                    break;
                case 6:
                    labels[i].setText(String.valueOf(unit.getValue_init() != null ? unit.getValue_init() : 0));
                    break;
            }
        }
        return this;
    }


    public void addData(WaterDataBean data) {
        for (int i = 0; i < labels.length; i++) {
            switch (i) {
                case 2:
                    labels[i].setText(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                    break;
                case 3:
                    labels[i].setText(String.valueOf(data.getTestValue()));
                    break;
                case 4:
                    labels[i].setText(String.valueOf(data.getValue1()));
                    break;
                case 5:
                    labels[i].setBackground(csuccess);
                    break;
            }
        }
    }
    public void stopConnect(){
        labels[5].setBackground(cfalse);
    }


}
