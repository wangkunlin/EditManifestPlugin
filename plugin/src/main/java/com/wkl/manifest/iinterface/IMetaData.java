package com.wkl.manifest.iinterface;

import com.wkl.manifest.config.MetaDataConfig;

import java.util.Map;

import groovy.lang.Closure;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-15
 */
public interface IMetaData extends IParseProperty {

    void metaData(Closure metaData);

    Map<String, MetaDataConfig> getMetaDataConfigs();
}
