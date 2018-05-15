package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IParseProperty;

import org.gradle.api.Project;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-15
 */
abstract class AbsConfigDelegate implements IParseProperty {
    Project mProject;

    AbsConfigDelegate(Project project) {
        mProject = project;
    }
}
