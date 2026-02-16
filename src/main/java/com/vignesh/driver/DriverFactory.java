package com.vignesh.driver;

import com.vignesh.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriver(String browser) {
        if (DRIVER.get() != null) {
            logger.warn("Driver already initialized for this thread. Quitting existing driver.");
            quitDriver();
        }

        WebDriver driver = createDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        DRIVER.set(driver);

        logger.info("WebDriver initialized: {} [Thread: {}]",
                browser, Thread.currentThread().getId());
    }

    private static WebDriver createDriver(String browser) {
        boolean headless = ConfigReader.isHeadless();

        return switch (browser.toLowerCase()) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.setExperimentalOption("prefs", java.util.Map.of(
                        "credentials_enable_service", false,
                        "profile.password_manager_enabled", false,
                        "profile.password_manager_leak_detection", false
                ));
                if (headless) {
                    options.addArguments("--headless=new");
                }
                yield new ChromeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.addPreference("dom.disable_open_during_load", false);
                options.addPreference("browser.link.open_newwindow", 3);
                options.addPreference("browser.link.open_newwindow.restriction", 0);
                if (headless) {
                    options.addArguments("--headless");
                }
                yield new FirefoxDriver(options);
            }
            
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                yield new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
            logger.info("WebDriver quit [Thread: {}]", Thread.currentThread().getId());
        }
    }
}
