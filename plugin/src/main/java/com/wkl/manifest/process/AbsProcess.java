package com.wkl.manifest.process;

import com.wkl.manifest.config.MetaDataConfig;
import com.wkl.manifest.config.RunWhere;
import com.wkl.manifest.iinterface.IMetaAttr;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;
import org.gradle.api.logging.Logger;

import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
abstract class AbsProcess {
    static final String ANDROID_PREFIX = "android:";

    Namespace mNamespace;
    Element mParent;

    AbsProcess(Namespace namespace, Element parent) {
        mNamespace = namespace;
        mParent = parent;
    }

    final void process(Logger logger, boolean debuggable) {
        String simpleName = getClass().getSimpleName();
        logger.lifecycle("start handle {}", simpleName);
        onHandle(logger, debuggable);
        logger.lifecycle("end handle {}", simpleName);
    }

    static boolean shouldRun(boolean debuggable, RunWhere runWhere) {
        return runWhere == RunWhere.ALL // 不管什么时候都执行
                || (debuggable && runWhere == RunWhere.DEBUG) // 只有 debug 执行
                || (!debuggable && runWhere == RunWhere.RELEASE); // 只有 release 执行
    }

    protected abstract void onHandle(Logger logger, boolean debuggable);

    private void handleMetaData(Element target, Map<String, MetaDataConfig> metaDataConfigs, Logger logger, boolean debuggable) {
        metaDataConfigs.forEach((s, config) -> new MetaDataProcess(mNamespace, target, config).process(logger, debuggable));
    }

    void handleCommon(Element target, IMetaAttr config, Logger logger, boolean debuggable) {
        if (shouldRun(debuggable, config.getRunWhere())) {
            handleAddAttr(target, config.getToAddAttrs(), logger);
            handleDelAttr(target, config.getToDelAttrs(), logger);
            handleModAttr(target, config.getToModAttrs(), logger);
        }
        handleMetaData(target, config.getMetaDataConfigs(), logger, debuggable);
    }

    private void handleAddAttr(Element target, Map<String, String> toAddAttr, Logger logger) {
        for (Map.Entry<String, String> node : toAddAttr.entrySet()) {
            String name = node.getKey();
            String value = node.getValue();
            Attribute attribute = target.attribute(name);
            if (attribute == null) {
                QName qName = new QName(name, mNamespace, ANDROID_PREFIX + name);
                attribute = new DefaultAttribute(qName);
                attribute.setValue(value);
                target.add(attribute);
                logger.info("attr {} not exists, add it", name);
            } else {
                attribute.setValue(value);
                logger.info("attr {} already exists, replace to {}", name, value);
            }
        }
    }

    private void handleDelAttr(Element target, Set<String> toDelAttr, Logger logger) {
        for (String del : toDelAttr) {
            Attribute attribute = target.attribute(del);
            if (attribute == null) {
                logger.info("attr {} not exists, skip it", del);
                continue;
            }
            target.addAttribute(del, null);
            logger.info("attr {} already exists, removed", del);
        }
    }

    private void handleModAttr(Element target, Map<String, String> toModAttr, Logger logger) {
        for (Map.Entry<String, String> node : toModAttr.entrySet()) {
            String name = node.getKey();
            String value = node.getValue();
            Attribute attribute = target.attribute(name);
            if (attribute == null) {
                logger.info("attr {} not exists, skip it, if should add it, use addAttr(name, value)", name);
                continue;
            }
            logger.info("attr {} already exists, replace to {}", name, value);
            attribute.setValue(value);
        }
    }
}
