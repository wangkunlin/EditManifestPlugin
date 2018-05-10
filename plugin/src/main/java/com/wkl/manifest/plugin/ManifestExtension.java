package com.wkl.manifest.plugin;


import com.wkl.manifest.config.ActivityConfig;
import com.wkl.manifest.config.ApplicationConfig;

import org.gradle.api.Action;
import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

import groovy.lang.Closure;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-16
 */
public class ManifestExtension {

    private ApplicationConfig mApplicationConfig;
    private List<ActivityConfig> mActivityConfigs = new ArrayList<>();
    private Project mProject;

    public ManifestExtension(Project project) {
        mApplicationConfig = new ApplicationConfig();
        mProject = project;
    }

    public void application(Action<ApplicationConfig> action) {
        action.execute(mApplicationConfig);
    }

    public ApplicationConfig getApplicationConfig() {
        return mApplicationConfig;
    }

    public static ManifestExtension getConfig(Project project) {
        return project.getExtensions().getByType(ManifestExtension.class);
    }

    public List<ActivityConfig> getActivityConfigs() {
        return mActivityConfigs;
    }

    public void activity(Closure closure) {
        ActivityConfig config = new ActivityConfig();
        mProject.configure(config, closure);
        mActivityConfigs.add(config);
    }

    public String parseProperty() {
        StringBuilder container = new StringBuilder("ManifestExtension:");
        mApplicationConfig.parseProperty(container);
        mActivityConfigs.forEach(activityConfig -> activityConfig.parseProperty(container));
        return container.toString();
    }
}
