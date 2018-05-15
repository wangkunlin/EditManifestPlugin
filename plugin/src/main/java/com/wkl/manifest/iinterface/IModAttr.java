package com.wkl.manifest.iinterface;

import java.util.Map;
import java.util.Set;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-15
 */
public interface IModAttr extends IParseProperty {
    void addAttr(String name, String value);

    void delAttr(String name);

    void modAttr(String name, String newValue);

    Map<String, String> getToAddAttrs();

    Map<String, String> getToModAttrs();

    Set<String> getToDelAttrs();
}
