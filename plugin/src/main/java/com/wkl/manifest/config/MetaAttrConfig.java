package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IMetaAttr;
import com.wkl.manifest.iinterface.IMetaData;
import com.wkl.manifest.iinterface.IModAttr;

import org.gradle.api.Project;

import java.util.Map;
import java.util.Set;

import groovy.lang.Closure;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
class MetaAttrConfig extends AbsConfig implements IMetaAttr {

    private IMetaData mMetaDelegate;
    private IModAttr mModAttrDelegate;

    MetaAttrConfig(Project project) {
        super(project);
        mMetaDelegate = new MetaDataConfigDelegate(project);
        mModAttrDelegate = new ModAttrConfigDelegate(project);
    }

    @Override
    public void parseProperty(StringBuilder container) {
        super.parseProperty(container);
        mMetaDelegate.parseProperty(container);
        mModAttrDelegate.parseProperty(container);
    }

    @Override
    public void metaData(Closure metaData) {
        mMetaDelegate.metaData(metaData);
    }

    @Override
    public Map<String, MetaDataConfig> getMetaDataConfigs() {
        return mMetaDelegate.getMetaDataConfigs();
    }

    @Override
    public void addAttr(String name, String value) {
        mModAttrDelegate.addAttr(name, value);
    }

    @Override
    public void delAttr(String name) {
        mModAttrDelegate.delAttr(name);
    }

    @Override
    public void modAttr(String name, String newValue) {
        mModAttrDelegate.modAttr(name, newValue);
    }

    @Override
    public Map<String, String> getToAddAttrs() {
        return mModAttrDelegate.getToAddAttrs();
    }

    @Override
    public Map<String, String> getToModAttrs() {
        return mModAttrDelegate.getToModAttrs();
    }

    @Override
    public Set<String> getToDelAttrs() {
        return mModAttrDelegate.getToDelAttrs();
    }
}
