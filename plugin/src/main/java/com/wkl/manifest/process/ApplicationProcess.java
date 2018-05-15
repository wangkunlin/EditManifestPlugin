package com.wkl.manifest.process;

import com.wkl.manifest.config.ActivityConfig;
import com.wkl.manifest.config.ApplicationConfig;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.gradle.api.logging.Logger;

import java.util.Map;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
class ApplicationProcess extends AbsProcess {

    private ApplicationConfig mConfig;
    private String mPackage;

    ApplicationProcess(String aPackage, Namespace namespace, Element parent, ApplicationConfig config) {
        super(namespace, parent);
        this.mConfig = config;
        mPackage = aPackage;
    }

    @Override
    void onHandle(Logger logger) {
        Element appNode = mParent.element("application");
        if (appNode == null) return;
        ApplicationConfig application = mConfig;
        if (application.mRemoved) {
            mParent.remove(appNode);
            logger.info("application removed, skip other action");
            return;
        }

        handleCommon(appNode, application, logger);

        Map<String, ActivityConfig> activities = application.getActivityConfigs();
        activities.forEach((s, config) -> new ActivityProcess(mNamespace, appNode, mPackage, config).process(logger));
    }
}
