package com.wkl.manifest.config;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-10
 */
abstract class AbsConfig {
    public boolean mRemoved;

    public void remove(boolean remove) {
        mRemoved = remove;
    }

    void parseProperty(StringBuilder container) {
        container.append(mRemoved);
    }
}
