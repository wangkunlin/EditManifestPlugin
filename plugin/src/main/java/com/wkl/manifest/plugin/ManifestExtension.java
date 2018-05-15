package com.wkl.manifest.plugin;


import com.wkl.manifest.config.ApplicationConfig;
import com.wkl.manifest.utils.Utils;

import org.gradle.api.Action;
import org.gradle.api.Project;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-16
 */
public class ManifestExtension {

    private ApplicationConfig mApplicationConfig;

    public ManifestExtension(Project project) {
        mApplicationConfig = new ApplicationConfig(project);
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

    public String parseProperty() {
        StringBuilder container = new StringBuilder("ManifestExtension:");
        mApplicationConfig.parseProperty(container);
        return Utils.generateMD5(container.toString());
    }
}
