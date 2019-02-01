package com.thingtek.start;

import com.thingtek.view.index.Index;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {
        String config = "appcontext/applicationContext.xml";
        ApplicationContext ac = new ClassPathXmlApplicationContext(config);
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ac.getBean(Index.class).init();
       /* SerialCollect serialCollect = ac.getBean(SerialCollect.class);
        serialCollect.readtest();*/

    }
}
