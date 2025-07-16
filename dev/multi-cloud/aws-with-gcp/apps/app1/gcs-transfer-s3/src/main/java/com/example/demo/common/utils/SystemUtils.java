package com.example.demo.common.utils;

public final class SystemUtils {

    private SystemUtils() {
        super();
    }

    public static String getEnv(String key) throws Exception {
        return System.getenv(key);
    }
}
