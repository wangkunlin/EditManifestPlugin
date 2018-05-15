package com.wkl.manifest.config;

import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

import groovy.lang.Closure;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class ApplicationConfig extends MetaAttrConfig {

    private Map<String, ActivityConfig> mActivityConfigs = new HashMap<>();

    public ApplicationConfig(Project project) {
        super(project);
    }

    public Map<String, ActivityConfig> getActivityConfigs() {
        return mActivityConfigs;
    }

    public void activity(Closure closure) {
        ActivityConfig config = new ActivityConfig(mProject);
        mProject.configure(config, closure);
        mActivityConfigs.put(config.mName, config);
    }

    @Override
    public void parseProperty(StringBuilder container) {
        super.parseProperty(container);
        mActivityConfigs.forEach((s, activityConfig) -> activityConfig.parseProperty(container));
    }
}
