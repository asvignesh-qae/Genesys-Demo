package com.vignesh.utils;

import com.vignesh.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = ConfigReader.getMaxRetryCount();
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("Retrying test '{}' - Attempt {} of {}",
                    result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
