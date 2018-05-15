package com.wkl.manifest.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by <a href="mailto:kunlin.wang@mtime.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
public final class Utils {
    private Utils() {
    }

    public static String generateMD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isTextEmpty(String cs) {
        return cs == null || cs.length() == 0;
    }
}
