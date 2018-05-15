package com.wkl.manifest.config;

import com.wkl.manifest.iinterface.IModAttr;

import org.gradle.api.Project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-15
 */
class ModAttrConfigDelegate extends AbsConfigDelegate implements IModAttr {

    ModAttrConfigDelegate(Project project) {
        super(project);
    }

    private Map<String, String> mToAddAttrs = new HashMap<>();
    private Map<String, String> mToModAttrs = new HashMap<>();
    private Set<String> mToDelAttrs = new HashSet<>();

    @Override
    public void addAttr(String name, String value) {
        mToAddAttrs.put(name, value);
    }

    @Override
    public void delAttr(String name) {
        mToDelAttrs.add(name);
    }

    @Override
    public void modAttr(String name, String newValue) {
        mToModAttrs.put(name, newValue);
    }

    @Override
    public Map<String, String> getToAddAttrs() {
        return mToAddAttrs;
    }

    @Override
    public Map<String, String> getToModAttrs() {
        return mToModAttrs;
    }

    @Override
    public Set<String> getToDelAttrs() {
        return mToDelAttrs;
    }

    @Override
    public void parseProperty(StringBuilder container) {
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
