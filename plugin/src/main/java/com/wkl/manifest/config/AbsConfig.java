package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IParseProperty;

import org.gradle.api.Project;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-10
 */
abstract class AbsConfig implements IParseProperty {

    Project mProject;
    public boolean mRemoved;

    AbsConfig(Project project) {
        mProject = project;
    }

    public void remove(boolean remove) {
        mRemoved = remove;
    }

    public void parseProperty(StringBuilder container) {
        container.append(mRemoved);
    }
}
