package com.wkl.manifest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:kunlin.wang@mtime.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class ApplicationConfig {

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

}
