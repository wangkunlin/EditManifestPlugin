package com.wkl.manifest;

import com.wkl.manifest.iinterface.ParseProperty;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-16
 */
public class ActivityConfig implements ParseProperty {
    public String name;
    public boolean removed;


    public void remove(boolean remove) {
        removed = remove;
    }

    public void name(String name) {
        this.name = name;
    }

    @Override
    public void parseProperty(StringBuilder container) {
        container.append("ActivityConfig:").append(name).append(removed);
    }
}
