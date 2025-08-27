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
    
    @Step("Verification: {description}")
    public static void verify(String description, Runnable verification) {
        logger.info("Verifying: {}", description);
        Allure.step("Verify: " + description, () -> {
            verification.run();
        });
    }
    
    public static void addInfo(String message) {
        logger.info(message);
        Allure.addAttachment("Information", message);
    }
}
