package com.wkl.manifest.config;

import org.gradle.api.Project;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
public class MetaDataConfig extends AbsConfig {

    public String mName;
    public String mValue;
    public String mResource;
    public String mRename;

    MetaDataConfig(Project project) {
        super(project);
    }

    public void name(String name) {
        this.mName = name;
    }

    public void rename(String name) {
        mRename = name;
    }

    public void value(String value) {
        mValue = value;
    }

    public void resource(String resource) {
        mResource = resource.startsWith("@") ? resource : "@" + resource;
    }

    @Override
    public void parseProperty(StringBuilder container) {
        super.parseProperty(container);
        container.append("MetaDataConfig:")
                .append(mName)
                .append(mRename)
                .append(mValue)
                .append(mResource);
    }
}
