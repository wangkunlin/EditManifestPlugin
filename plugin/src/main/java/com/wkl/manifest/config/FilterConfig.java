package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IModAttr;

import org.gradle.api.Project;

import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-15
 */
public class FilterConfig extends AbsConfig implements IModAttr {

    FilterConfig(Project project) {
        super(project);
        mModAttrDelegate = new ModAttrConfigDelegate(project);
    }

    private IModAttr mModAttrDelegate;

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

    @Override
    public void parseProperty(StringBuilder container) {
        super.parseProperty(container);
        mModAttrDelegate.parseProperty(container);
    }
}
