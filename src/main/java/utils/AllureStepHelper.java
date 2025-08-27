package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllureStepHelper {
    private static final Logger logger = LoggerFactory.getLogger(AllureStepHelper.class);
    
    @Step("Step: {stepDescription}")
    public static void step(String stepDescription, Runnable action) {
        logger.info("Executing step: {}", stepDescription);
        Allure.step(stepDescription, () -> {
            action.run();
        });
    }
    
    @Step("Step with Screenshot: {stepDescription}")
    public static void stepWithScreenshot(String stepDescription, Runnable action) {
        logger.info("Executing step with screenshot: {}", stepDescription);
        Allure.step(stepDescription, () -> {
            action.run();
            // Take screenshot after step execution
            ScreenshotHelper.takeScreenshot("Step: " + stepDescription);
        });
    }
    
    @Step("Verification: {description}")
    public static void verify(String description, Runnable verification) {
        logger.info("Verifying: {}", description);
        Allure.step("Verify: " + description, () -> {
            verification.run();
            // Take screenshot after verification
            ScreenshotHelper.takeScreenshot("Verification: " + description);
        });
    }
    
    @Step("Critical Step: {stepDescription}")
    public static void criticalStep(String stepDescription, Runnable action) {
        logger.info("Executing critical step: {}", stepDescription);
        
        // Take screenshot before critical step
        ScreenshotHelper.takeScreenshot("Before Critical Step: " + stepDescription);
        
        Allure.step(stepDescription, () -> {
            try {
                action.run();
                // Take screenshot after successful critical step
                ScreenshotHelper.takeScreenshot("After Critical Step: " + stepDescription);
            } catch (Exception e) {
                // Take screenshot on failure
                ScreenshotHelper.takeScreenshot("Critical Step Failed: " + stepDescription);
                ScreenshotHelper.attachText("Critical Step Error", e.getMessage());
                throw e;
            }
        });
    }
    
    public static void addInfo(String message) {
        logger.info(message);
        Allure.addAttachment("Information", "text/plain", message, "txt");
    }
    
    public static void addInfoWithScreenshot(String message) {
        logger.info(message);
        Allure.addAttachment("Information", "text/plain", message, "txt");
        ScreenshotHelper.takeScreenshot("Info: " + message);
    }
    
    @Step("Navigation: {description}")
    public static void navigationStep(String description, Runnable action) {
        logger.info("Navigation step: {}", description);
        Allure.step("Navigate: " + description, () -> {
            action.run();
            // Always take screenshot after navigation
            ScreenshotHelper.takeScreenshot("Navigation: " + description);
        });
    }
    
    @Step("Wait and Verify: {description}")
    public static void waitAndVerify(String description, Runnable waitAction, Runnable verification) {
        logger.info("Wait and verify: {}", description);
        Allure.step("Wait and Verify: " + description, () -> {
            // Execute wait action
            waitAction.run();
            // Take screenshot before verification
            ScreenshotHelper.takeScreenshot("Before Verification: " + description);
            // Execute verification
            verification.run();
            // Take screenshot after verification
            ScreenshotHelper.takeScreenshot("After Verification: " + description);
        });
    }
}
