package com.wkl.manifest;

import com.wkl.manifest.iinterface.ParseProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class ApplicationConfig implements ParseProperty {

    private Map<String, String> mToAddAttr = new HashMap<>();
    private Map<String, String> mToModAttr = new HashMap<>();
    private Set<String> mToDelAttr = new HashSet<>();

    public void addAttr(String name, String value) {
        mToAddAttr.put(name, value);
    }

    public void delAttr(String name) {
        mToDelAttr.add(name);
    }

    public void modAttr(String name, String newValue) {
        mToModAttr.put(name, newValue);
    }

    public Map<String, String> getToAddAttr() {
        return mToAddAttr;
    }

    public Map<String, String> getToModAttr() {
        return mToModAttr;
    }

    public Set<String> getToDelAttr() {
        return mToDelAttr;
    }

    @Override
    public void parseProperty(StringBuilder container) {
        container.append("ApplicationConfig:");
        fillProperty(container, mToAddAttr, "ToAddAttr");
        fillProperty(container, mToModAttr, "ToModAttr");
        fillProperty(container, mToDelAttr, "ToAddAttr");
    }

    private void fillProperty(StringBuilder sb, Map<String, String> attr, String name) {
        sb.append(name).append("%");
        attr.forEach((k, v) -> sb.append(k).append('-').append(v).append("#"));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("@");
    }

    private void fillProperty(StringBuilder sb, Set<String> attr, String name) {
        sb.append(name).append("%");
        attr.forEach(v -> sb.append(v).append('-').append("#"));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("@");
    }
}
