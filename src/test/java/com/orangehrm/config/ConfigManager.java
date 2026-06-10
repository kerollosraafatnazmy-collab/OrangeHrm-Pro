package com.orangehrm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream(
                    "src/test/resources/config.properties"
            );
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found!");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getClientId() {
        return get("client.id");
    }

    public static String getRedirectUri() {
        return get("redirect.uri");
    }
}