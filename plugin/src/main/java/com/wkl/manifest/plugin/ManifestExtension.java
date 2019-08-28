package com.wkl.manifest.plugin;


import com.wkl.manifest.config.ApplicationConfig;
import com.wkl.manifest.utils.Utils;

import org.gradle.api.Action;
import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-16
 */
public class ManifestExtension {

    private ApplicationConfig mApplicationConfig;
    private String mPackageName;
    private Map<String, String> mToReplace = new HashMap<>();

    public ManifestExtension(Project project) {
        mApplicationConfig = new ApplicationConfig(project);
    }

    public void application(Action<ApplicationConfig> action) {
        action.execute(mApplicationConfig);
    }

    public void packageName(String packageName) {
        mPackageName = packageName.trim();
    }

    public void replace(String from, String to) {
        mToReplace.put(from.trim(), to.trim());
    }

    public Map<String, String> getToReplace() {
        return mToReplace;
    }

    public String getPackageName() {
        return mPackageName;
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
        container.append(mPackageName);
        fillProperty(container, mToReplace, "replace");
        return Utils.generateMD5(container.toString());
    }

    @SuppressWarnings("SameParameterValue")
    private static void fillProperty(StringBuilder sb, Map<String, String> attr, String name) {
        sb.append(name).append("%");
        attr.forEach((k, v) -> sb.append(k).append('-').append(v).append("#"));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("@");
    }
}
