package com.wkl.manifest.config;

import org.gradle.api.Project;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
class ComponentConfig extends AbsAppConfig {

    public String mName;

    ComponentConfig(Project project) {
        super(project);
    }

    public void name(String name) {
        this.mName = name;
    }

    @Override
    public void parseProperty(StringBuilder container) {
        container.append(mName);
    }

}
