package tests;

import config.ConfigManager;
import config.WebDriverManager;
import io.qameta.allure.Epic;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pages.HomePage;
import utils.PopupHandler;

import static com.codeborne.selenide.Selenide.*;

@Epic("Tiki E-commerce Testing")
@Listeners({AllureTestNg.class})
public abstract class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected HomePage homePage;
    
    @BeforeMethod
    public void setUp() {
        logger.info("Setting up test environment");
        WebDriverManager.setupBrowserFromConfig();
        open(ConfigManager.getBaseUrl());
        
        // Handle any initial popups that appear on page load (only if visible)
        PopupHandler.closePopupIfVisible();
        PopupHandler.waitForPageInteractive();
        
        homePage = new HomePage();
    }
    
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down test environment");
        closeWebDriver();
    }
}
