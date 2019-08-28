package com.wkl.manifest.process;

import com.wkl.manifest.config.ActivityConfig;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.gradle.api.logging.Logger;

import java.util.List;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
class ActivityProcess extends AbsProcess {

    private ActivityConfig mConfig;
    private String mPackage;

    ActivityProcess(Namespace namespace, Element parent, String apackage, ActivityConfig config) {
        super(namespace, parent);
        mPackage = apackage;
        mConfig = config;
    }

    @Override
    protected void onHandle(Logger logger, boolean debuggable) {
        List<Element> activities = mParent.elements("activity");
        ActivityConfig config = mConfig;
        Element targetAct = null;
        for (Element act : activities) {
            String actName = act.attribute("name").getValue();
            if (actName.equals(config.mName)
                    || config.mName.equals(mPackage + actName)
                    || actName.equals(mPackage + config.mName)) {
                targetAct = act;
                break;
            }
        }

        if (targetAct == null) {
            // not found
            logger.info("act not found {}", config.mName);
            return;
        }

        if (shouldRun(debuggable, mConfig.getRunWhere())) {
            if (mConfig.isRemoved()) {
                mParent.remove(targetAct);
                logger.info("activity {} removed, skip other action", mConfig.mName);
                return;
            }
        }

        handleCommon(targetAct, mConfig, logger, debuggable);
    }

}
