package com.vignesh.utils;

import com.vignesh.config.ConfigReader;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    public static String capture(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String dirPath = ConfigReader.getScreenshotPath();

        try {
            Path dir = Path.of(dirPath);
            Files.createDirectories(dir);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destPath = dir.resolve(fileName);
            Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Screenshot saved: {}", destPath);
            return destPath.toAbsolutePath().toString();
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for: {}", testName, e);
            return null;
        }
    }

    public static String captureAsBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static void attachScreenshotToAllure(WebDriver driver, String name) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
    }
}
