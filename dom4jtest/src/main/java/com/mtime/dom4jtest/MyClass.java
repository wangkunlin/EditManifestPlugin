package com.mtime.dom4jtest;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MyClass {


    private static final String M = "/Users/mtime/AndroidStuidoProjects/ManifestEditor/dom4jtest/AndroidManifest.xml";
    private static final String N = "/Users/mtime/AndroidStuidoProjects/ManifestEditor/dom4jtest/EditAndroidManifest.xml";

    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(M));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getRootElement();
        Attribute aPackage = rootElement.attribute("package");

        print(aPackage.getValue());
        handleActivities(document);
        Element appNode = rootElement.element("application");

//        QName allowQname = new QName("allowBackup", null, "android:allowBackup");

//        Attribute allowBackup = appNode.attribute("allowBackup");

//        print("allowBackup " + (allowBackup == null));

//        allowBackup.setValue("false");
//        Attribute attributeallowBackup = new DefaultAttribute(allowQname, "false");
//        appNode.add(attributeallowBackup);
        appNode.addAttribute("allowBackup", null);

        try {
            FileWriter writer = new FileWriter(new File(N));
            document.write(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void handleActivities(Document document) {
        Element rootElement = document.getRootElement();
        String pkg = rootElement.attribute("package").getValue();

        Element appNode = rootElement.element("application");
        List<Element> activities = appNode.elements("activity");

//        for (ActivityConfig config : activityConfigs) {
        for (Element act : activities) {
            String actName = act.attribute("name").getValue();
            print(actName);
            if (actName.equals(".MainActivity")) {
                performActivity(appNode, act);
            } else if (".MainActivity".equals(pkg + actName)) {
                performActivity(appNode, act);
            } else {
                // not found
            }
        }
//        }
    }

    private static void performActivity(Element appNode, Element activity) {
        appNode.remove(activity);
        print("remove");
    }

    private static void print(String info) {
        System.out.println(info);
    }
}
