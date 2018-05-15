package com.wkl.manifest.config;

import org.gradle.api.Project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import groovy.lang.Closure;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
public class AbsAppConfig extends AbsConfig {

    Project mProject;

    private Map<String, String> mToAddAttrs = new HashMap<>();
    private Map<String, String> mToModAttrs = new HashMap<>();
    private Set<String> mToDelAttrs = new HashSet<>();

    private Map<String, MetaDataConfig> mMetaDataConfigs = new HashMap<>();

    AbsAppConfig(Project project) {
        mProject = project;
    }

    public void metaData(Closure metaData) {
        MetaDataConfig metaDataConfig = new MetaDataConfig();
        mProject.configure(metaDataConfig, metaData);
        mMetaDataConfigs.put(metaDataConfig.mName, metaDataConfig);
    }

    public void addAttr(String name, String value) {
        mToAddAttrs.put(name, value);
    }

    public void delAttr(String name) {
        mToDelAttrs.add(name);
    }

    public void modAttr(String name, String newValue) {
        mToModAttrs.put(name, newValue);
    }

    public Map<String, String> getToAddAttrs() {
        return mToAddAttrs;
    }

    public Map<String, String> getToModAttrs() {
        return mToModAttrs;
    }

    public Set<String> getToDelAttrs() {
        return mToDelAttrs;
    }

    public Map<String, MetaDataConfig> getMetaDataConfigs() {
        return mMetaDataConfigs;
    }

    @Override
    public void parseProperty(StringBuilder container) {
        container.append(getClass().getSimpleName()).append(":");
        mMetaDataConfigs.forEach((s, metaDataConfig) -> metaDataConfig.parseProperty(container));
        fillProperty(container, mToAddAttrs, "ToAddAttrs");
        fillProperty(container, mToModAttrs, "ToModAttrs");
        fillProperty(container, mToDelAttrs, "ToAddAttrs");
    }

    private static void fillProperty(StringBuilder sb, Map<String, String> attr, String name) {
        sb.append(name).append("%");
        attr.forEach((k, v) -> sb.append(k).append('-').append(v).append("#"));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("@");
    }

    private static void fillProperty(StringBuilder sb, Set<String> attr, String name) {
        sb.append(name).append("%");
        attr.forEach(v -> sb.append(v).append('-').append("#"));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("@");
    }
}
