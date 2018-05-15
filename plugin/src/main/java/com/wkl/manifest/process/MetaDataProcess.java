package com.wkl.manifest.process;

import com.wkl.manifest.config.MetaDataConfig;
import com.wkl.manifest.utils.Utils;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;
import org.gradle.api.logging.Logger;

import java.util.List;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
class MetaDataProcess extends AbsProcess {

    private static final String META_DATA = "meta-data";

    private MetaDataConfig mConfig;

    MetaDataProcess(Namespace namespace, Element parent, MetaDataConfig config) {
        super(namespace, parent);
        mConfig = config;
    }

    @Override
    void onHandle(Logger logger) {
        List<Element> elements = mParent.elements(META_DATA);

        for (Element meta : elements) {
            String name = meta.attribute("name").getValue();
            if (mConfig.mName.equals(name)) {
                if (mConfig.mRemoved) {
                    mParent.remove(meta);
                    logger.info("meta-data {} removed, skip other action", mConfig.mName);
                    return;
                }
                handleMetaData(meta, false);
                return;
            }
        }

        // not found
        if (Utils.isTextEmpty(mConfig.mName) || mConfig.mRemoved) {
            return;
        }
        Element meta = new DefaultElement(META_DATA);
        handleMetaData(meta, true);
        mParent.add(meta);
    }

    private void handleMetaData(Element meta, boolean create) {
        if (create) {
            addOrReplace(meta, "name", mConfig.mName);
        }
        addOrReplace(meta, "name", mConfig.mRename);
        addOrReplace(meta, "value", mConfig.mValue);
        addOrReplace(meta, "resource", mConfig.mResource);
    }

    private void addOrReplace(Element meta, String name, String value) {
        if (!Utils.isTextEmpty(value)) {
            QName qName = new QName(name, mNamespace, ANDROID_PREFIX + name);
            Attribute aName = meta.attribute(qName);
            if (aName == null) {
                aName = new DefaultAttribute(qName);
                aName.setValue(value);
                meta.add(aName);
            } else {
                aName.setValue(value);
            }
        }
    }
}
