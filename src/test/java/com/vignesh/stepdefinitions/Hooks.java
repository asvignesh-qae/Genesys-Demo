package com.vignesh.stepdefinitions;

import com.vignesh.config.ConfigReader;
import com.vignesh.driver.DriverFactory;
import com.vignesh.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        logger.info("=== Starting Scenario: {} ===", scenario.getName());
        DriverFactory.initDriver(ConfigReader.getBrowser());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && DriverFactory.getDriver() != null) {
            String base64 = ScreenshotUtils.captureAsBase64(DriverFactory.getDriver());
            if (base64 != null) {
                scenario.attach(
                        java.util.Base64.getDecoder().decode(base64),
                        "image/png",
                        scenario.getName()
                );
            }
            logger.error("Scenario FAILED: {}", scenario.getName());
        } else {
            logger.info("Scenario PASSED: {}", scenario.getName());
        }
        DriverFactory.quitDriver();
        logger.info("=== Finished Scenario: {} ===", scenario.getName());
    }
}
