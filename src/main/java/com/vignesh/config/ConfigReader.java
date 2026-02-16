package com.vignesh.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(is);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null) {
            return systemProp;
        }
        return properties.getProperty(key);
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static String getBaseUrl() {
        return get("base_url");
    }

    public static String getApiBaseUrl() {
        return get("api_base_url");
    }

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicit_wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page_load_timeout"));
    }

    public static boolean doITakeScreenshotsOnFailure() {
        return Boolean.parseBoolean(get("screenshot.on.failure"));
    }

    public static String getScreenshotPath() {
        return get("screenshot_path");
    }

    public static int getMaxRetryCount() {
        return Integer.parseInt(get("max_retry_count"));
    }

}
