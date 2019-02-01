package com.thingtek.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VoiceConfig {

    private static String fileName = "propertyInfo/properties.xml";
    private static String jfstr = "";
    private static boolean VioceWarn;// 报警


    public static String getJfstr() {
        return jfstr;
    }

    /**
     * 设置声音报警
     *
     */
    public static void setVoiceWarn(boolean b) {
        VioceWarn = b;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(fileName));
            Element warning = (Element) document.selectSingleNode("//collection/warning");
            if (warning != null) {
                warning.addAttribute("voice", String.valueOf(b));
            }
            push2file(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void push2file(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("gb2312");
        XMLWriter writer = new XMLWriter(new FileOutputStream(fileName), format);
        writer.write(document);
        writer.close();
    }

    /**
     * 是否声音报警
     *
     * @return
     */
    public static boolean isVioceWarn() {
        return VioceWarn;
    }

}
