package tests;

import config.ConfigManager;
import config.WebDriverManager;
import io.qameta.allure.Epic;
import listeners.AllureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pages.HomePage;
import utils.PopupHandler;
import utils.ScreenshotHelper;
import utils.VideoRecorder;

import java.lang.reflect.Method;

import static com.codeborne.selenide.Selenide.*;

@Epic("Tiki E-commerce Testing")
@Listeners({AllureListener.class})
public abstract class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected HomePage homePage;
    
    @BeforeMethod
    public void setUp(Method method) {
        String testName = method.getName();
        logger.info("Setting up test environment for: {}", testName);
        
        // Start video recording (placeholder for now)
        VideoRecorder.startRecording(testName);
        
        WebDriverManager.setupBrowserFromConfig();
        open(ConfigManager.getBaseUrl());
        
        // Handle any initial popups that appear on page load (only if visible)
        PopupHandler.closePopupIfVisible();
        PopupHandler.waitForPageInteractive();
        
        homePage = new HomePage();
        
        // Take initial screenshot
        ScreenshotHelper.takeScreenshot("Test Start - " + testName);
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        boolean testFailed = !result.isSuccess();
        
        logger.info("Tearing down test environment for: {} - Status: {}", 
            testName, testFailed ? "FAILED" : "PASSED");
        
        // Take final screenshot
        if (testFailed) {
            ScreenshotHelper.takeScreenshot("Test Failure - " + testName);
            ScreenshotHelper.attachText("Test Failure Reason", 
                result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown failure");
        } else {
            ScreenshotHelper.takeScreenshot("Test Success - " + testName);
        }
        
        // Stop video recording and attach if test failed
        VideoRecorder.stopAndAttachVideo(testName, testFailed);
        
        closeWebDriver();
    }
}
