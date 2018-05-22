package com.consort.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceNameNormalizer {
    public static String normalizeServiceName(String serviceName) {
        Pattern p = Pattern.compile("(.*?(?=(-v1)|$))");
        Matcher m = p.matcher(serviceName);
        if (m.find()) {
            return m.group(1);
        }
        return serviceName;
    }
}