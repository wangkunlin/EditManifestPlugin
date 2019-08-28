package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IParseProperty;
import com.wkl.manifest.iinterface.IRunWhere;

import org.gradle.api.Project;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-10
 */
abstract class AbsConfig implements IParseProperty, IRunWhere {

    Project mProject;
    private boolean mRemoved = false;
    private RunWhere mRunWhere = RunWhere.RELEASE;

    AbsConfig(Project project) {
        mProject = project;
    }

    public void remove(boolean remove) {
        mRemoved = remove;
    }

    public boolean isRemoved() {
        return mRemoved;
    }

    @Override
    public RunWhere getRunWhere() {
        return mRunWhere;
    }

    @Override
    public void runWhere(RunWhere runWhere) {
        mRunWhere = runWhere;
    }

    public void parseProperty(StringBuilder container) {
        container.append(mRemoved);
        container.append(mRunWhere);
    }
}
