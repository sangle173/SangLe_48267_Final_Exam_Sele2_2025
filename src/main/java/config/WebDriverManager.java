package config;

import com.codeborne.selenide.Configuration;
import enums.BrowserType;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverManager {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverManager.class);
    
    @Step("Setup browser: {browserType}")
    public static void setupBrowser(BrowserType browserType) {
        logger.info("Setting up browser: {}", browserType);
        
        switch (browserType) {
            case CHROME:
                Configuration.browser = "chrome";
                Configuration.browserSize = "1920x1080";
                Configuration.headless = false;
                break;
            case FIREFOX:
                Configuration.browser = "firefox";
                Configuration.browserSize = "1920x1080";
                Configuration.headless = false;
                break;
            default:
                throw new RuntimeException("Unsupported browser: " + browserType);
        }
        
        Configuration.timeout = 10000;
        Configuration.screenshots = true;
        Configuration.reportsFolder = "target/screenshots";
    }
    
    @Step("Setup browser from config")
    public static void setupBrowserFromConfig() {
        String browserName = ConfigManager.getBrowser();
        BrowserType browserType = BrowserType.valueOf(browserName.toUpperCase());
        setupBrowser(browserType);
    }
}
