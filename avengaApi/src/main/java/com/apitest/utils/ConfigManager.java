package com.apitest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final String DEFAULT_ENV = "qa";
    
    static {
        loadConfiguration();
    }
    
    private static void loadConfiguration() {
        String environment = System.getProperty("env", System.getenv("ENV") != null ? System.getenv("ENV") : DEFAULT_ENV);
        
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + environment + ".properties")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
        }
        
        properties.putAll(System.getProperties());
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getEnvironment() {
        return getProperty("ENV");
    }
    
    public static String getBaseUrl() {
        return getProperty("BASE_URL");
    }
    
    public static String getUsername() {
        return getProperty("USERNAME");
    }
    
    public static String getPassword() {
        return getProperty("PASSWORD");
    }
    
    public static String getApiKey() {
        return getProperty("API_KEY");
    }
    
    public static String getBasePath() {
        return getProperty("BASE_PATH");
    }
}