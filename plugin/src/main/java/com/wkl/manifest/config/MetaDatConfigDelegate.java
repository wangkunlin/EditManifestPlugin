package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IMetaData;

import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

import groovy.lang.Closure;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-15
 */
class MetaDatConfigDelegate extends AbsConfigDelegate implements IMetaData {

    MetaDatConfigDelegate(Project project) {
        super(project);
    }
    private Map<String, MetaDataConfig> mMetaDataConfigs = new HashMap<>();

    @Override
    public void metaData(Closure metaData) {
        MetaDataConfig metaDataConfig = new MetaDataConfig(mProject);
        mProject.configure(metaDataConfig, metaData);
        mMetaDataConfigs.put(metaDataConfig.mName, metaDataConfig);
    }

    @Override
    public Map<String, MetaDataConfig> getMetaDataConfigs() {
        return mMetaDataConfigs;
    }

    @Override
    public void parseProperty(StringBuilder container) {
        mMetaDataConfigs.forEach((s, metaDataConfig) -> metaDataConfig.parseProperty(container));
    }
}
