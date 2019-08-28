package com.wkl.manifest.config;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2019-08-28
 */
public enum RunWhere {

    RELEASE("release"), // run on debuggable = false

    DEBUG("debug"), // run on debuggable = true

    ALL("all"); // run on all

    private String mLabel;

    RunWhere(String text) {
        this.mLabel = text;
    }

    @Override
    public String toString() {
        return "RunWhere{" +
                "mLabel='" + mLabel + '\'' +
                '}';
    }

}
