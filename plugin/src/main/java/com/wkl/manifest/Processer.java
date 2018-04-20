package com.wkl.manifest;

import com.android.build.gradle.tasks.ManifestProcessorTask;
import com.wkl.manifest.plugin.ManifestExtension;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:kunlin.wang@mtime.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class Processer {
    private static final String ANDROID_PREFIX = "android:";
    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";
    private ManifestExtension mConfig;
    @InputFile
    private File mManifest;
    @OutputFile
    private File mOutXml;

    //    private boolean mIsUp3_0;
    private Project mProject;

    public Processer(Project project, ManifestProcessorTask task, boolean isUp3_0) {
        if (isUp3_0) { // 3.0 以上版本 getManifestOutputFile() 被弃用
            mOutXml = new File(task.getManifestOutputDirectory(), "Edit" + ANDROID_MANIFEST);
            mManifest = new File(task.getManifestOutputDirectory(), ANDROID_MANIFEST);
        } else {
            mOutXml = new File(task.getManifestOutputFile().getParentFile(), "Edit" + ANDROID_MANIFEST);
            mManifest = new File(task.getManifestOutputFile().getAbsolutePath());
        }
        mConfig = ManifestExtension.getConfig(project);
//        mIsUp3_0 = isUp3_0;
        mProject = project;
    }

    public void run() throws Exception {
        if (mOutXml.exists()) {
            mOutXml.delete();
        }
        // 使用 dom4j 处理 xml
        SAXReader reader = new SAXReader();
        Document document = reader.read(mManifest);

        ApplicationConfig application = mConfig.getApplication();
        handleApplication(application, document);
        handleActivities(mConfig.getActivityConfigs(), document);
        // 输出到文件
        FileWriter out = new FileWriter(mOutXml);
        document.write(out);
        out.flush();
        out.close();
        File manifestTmp = new File(mManifest.getAbsolutePath() + ".tmp");
        mManifest.renameTo(manifestTmp);
        if (mOutXml.renameTo(mManifest)) {
            manifestTmp.delete();
        } else {
            manifestTmp.renameTo(mManifest);
            mOutXml.delete();
//            throw new RuntimeException("失败");
        }
    }

    private void handleActivities(List<ActivityConfig> activityConfigs,
                                  Document document) {
        Logger logger = mProject.getLogger();
        Element rootElement = document.getRootElement();
        String pkg = rootElement.attribute("package").getValue();
        logger.info("pkg " + pkg);

        Element appNode = rootElement.element("application");
        List<Element> activities = appNode.elements("activity");

        for (ActivityConfig config : activityConfigs) {
            for (Element act : activities) {
                String actName = act.attribute("name").getValue();
                if (actName.equals(config.name)) {
                    performActivity(appNode, act, config);
                } else if (config.name.equals(pkg + actName)) {
                    performActivity(appNode, act, config);
                } else if (actName.equals(pkg + config.name)) {
                    performActivity(appNode, act, config);
                } else {
                    // not found
                    logger.info("act not found " + config.name);
                }
            }
        }
    }

    private void performActivity(Element appNode, Element activity, ActivityConfig config) {
        if (config.removed) {
            appNode.remove(activity);
        }
    }

    private void handleApplication(ApplicationConfig application, Document document) {
        Element rootElement = document.getRootElement();
        Namespace namespace = rootElement.getNamespace();
        Element appNode = rootElement.element("application");

        Map<String, String> toAddAttr = application.getToAddAttr();
        for (Map.Entry<String, String> node : toAddAttr.entrySet()) {
            String name = node.getKey();
            String value = node.getValue();
            Attribute attribute = appNode.attribute(name);
            if (attribute == null) {
                QName qName = new QName(name, namespace, ANDROID_PREFIX + name);
                attribute = new DefaultAttribute(qName);
                attribute.setValue(value);
                appNode.add(attribute);
            } else {
                attribute.setValue(value);
            }
        }

        Set<String> toDelAttr = application.getToDelAttr();
        for (String del : toDelAttr) {
            Attribute attribute = appNode.attribute(del);
            if (attribute == null) {
                continue;
            }
            appNode.addAttribute(del, null);
        }

        Map<String, String> toModAttr = application.getToModAttr();
        for (Map.Entry<String, String> node : toModAttr.entrySet()) {
            String name = node.getKey();
            String value = node.getValue();
            Attribute attribute = appNode.attribute(name);
            if (attribute == null) {
                continue;
            }
            attribute.setValue(value);
        }
    }
}
