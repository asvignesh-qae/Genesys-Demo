package com.vignesh.base;

import com.vignesh.config.ConfigReader;
import com.vignesh.driver.DriverFactory;
import com.vignesh.listeners.TestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners(TestListener.class)
public class BaseTest {

    protected final Logger logger = LogManager.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        String targetBrowser = (browser != null && !browser.isEmpty())
                ? browser : ConfigReader.getBrowser();
        logger.info("=== Setting up WebDriver [{}] ===", targetBrowser);
        DriverFactory.initDriver(targetBrowser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("=== Tearing down WebDriver ===");
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
