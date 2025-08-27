package utils;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class PopupHandler {
    private static final Logger logger = LoggerFactory.getLogger(PopupHandler.class);
    
    // Common popup close button selectors
    private static final String[] CLOSE_BUTTON_SELECTORS = {
        "//img[@alt='close-icon']",
        "//div[@class='sc-cbd12f50-2 gLMejz']//img[@alt='close-icon']",
        "//button[contains(@class,'close')]",
        "//div[contains(@class,'close')]",
        "//span[contains(@class,'close')]",
        "//i[contains(@class,'close')]",
        "//div[@role='dialog']//button",
        "//div[contains(@class,'modal')]//button[contains(@class,'close')]",
        "//div[contains(@class,'popup')]//button[contains(@class,'close')]",
        "//button[@aria-label='Close']",
        "//button[@title='Close']",
        "//div[@class='modal-close']",
        "//div[@class='popup-close']"
    };
    
    // Common popup container selectors
    private static final String[] POPUP_CONTAINER_SELECTORS = {
        "//div[@class='sc-cbd12f50-2 gLMejz']",
        "//div[contains(@class,'modal')]",
        "//div[contains(@class,'popup')]",
        "//div[@role='dialog']",
        "//div[contains(@class,'overlay')]",
        "//div[contains(@class,'backdrop')]"
    };
    
    @Step("Close any visible popups")
    public static void closeAnyVisiblePopup() {
        logger.info("Checking for visible popups");
        
        try {
            // Quick check if any popup containers are visible first
            boolean popupFound = false;
            String foundPopupSelector = null;
            
            for (String selector : POPUP_CONTAINER_SELECTORS) {
                SelenideElement popup = $x(selector);
                if (popup.exists() && popup.isDisplayed()) {
                    logger.info("Found visible popup with selector: {}", selector);
                    popupFound = true;
                    foundPopupSelector = selector;
                    break;
                }
            }
            
            if (!popupFound) {
                logger.debug("No popups found - skipping close actions");
                return;
            }
            
            logger.info("Attempting to close popup: {}", foundPopupSelector);
            
            // Try to close the popup using various close button selectors
            boolean popupClosed = false;
            for (String selector : CLOSE_BUTTON_SELECTORS) {
                SelenideElement closeButton = $x(selector);
                if (closeButton.exists() && closeButton.isDisplayed()) {
                    logger.info("Clicking close button with selector: {}", selector);
                    closeButton.click();
                    
                    // Wait a bit to see if popup is closed
                    sleep(1000);
                    
                    // Check if popup is still visible
                    SelenideElement originalPopup = $x(foundPopupSelector);
                    if (!originalPopup.exists() || !originalPopup.isDisplayed()) {
                        logger.info("Popup successfully closed");
                        popupClosed = true;
                        break;
                    }
                }
            }
            
            if (!popupClosed) {
                // If close button didn't work, try pressing ESC key
                logger.info("Close button didn't work, trying ESC key");
                actions().sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
                sleep(1000);
                
                // Check if popup is closed after ESC
                SelenideElement originalPopup = $x(foundPopupSelector);
                if (!originalPopup.exists() || !originalPopup.isDisplayed()) {
                    logger.info("Popup closed with ESC key");
                    popupClosed = true;
                }
            }
            
            if (!popupClosed) {
                // If ESC didn't work, try clicking outside the popup
                logger.info("ESC key didn't work, trying to click outside popup");
                $("body").click();
                sleep(1000);
                
                // Final check
                SelenideElement originalPopup = $x(foundPopupSelector);
                if (!originalPopup.exists() || !originalPopup.isDisplayed()) {
                    logger.info("Popup closed by clicking outside");
                } else {
                    logger.warn("Unable to close popup with selector: {}", foundPopupSelector);
                }
            }
            
        } catch (Exception e) {
            logger.warn("Error while trying to close popup: {}", e.getMessage());
        }
    }
    
    @Step("Close specific popup using Tiki's close icon")
    public static void closeTikiPopup() {
        logger.info("Checking for Tiki-specific popup");
        
        try {
            // Specific selector for Tiki popup close button
            SelenideElement tikiCloseButton = $x("//div[@class='sc-cbd12f50-2 gLMejz']//img[@alt='close-icon']");
            
            if (tikiCloseButton.exists() && tikiCloseButton.isDisplayed()) {
                logger.info("Found Tiki popup close button, clicking it");
                tikiCloseButton.click();
                sleep(1000);
                logger.info("Tiki popup closed successfully");
            } else {
                logger.debug("Tiki popup close button not found or not visible");
            }
            
        } catch (Exception e) {
            logger.warn("Error while trying to close Tiki popup: {}", e.getMessage());
        }
    }
    
    @Step("Check if any popup is visible")
    public static boolean isPopupVisible() {
        try {
            for (String selector : POPUP_CONTAINER_SELECTORS) {
                SelenideElement popup = $x(selector);
                if (popup.exists() && popup.isDisplayed()) {
                    logger.debug("Popup found with selector: {}", selector);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.warn("Error while checking for popups: {}", e.getMessage());
            return false;
        }
    }
    
    @Step("Close popup only if visible")
    public static void closePopupIfVisible() {
        if (isPopupVisible()) {
            logger.info("Popup detected, attempting to close");
            closeAnyVisiblePopup();
        } else {
            logger.debug("No popup visible, skipping close action");
        }
    }
    
    @Step("Wait for page to be interactive (no blocking popups)")
    public static void waitForPageInteractive() {
        logger.info("Waiting for page to become interactive");
        
        int attempts = 0;
        int maxAttempts = 3; // Reduced from 5 to 3 for efficiency
        
        while (attempts < maxAttempts) {
            // Only check for popups, don't auto-close them here
            boolean hasBlockingPopup = false;
            for (String selector : POPUP_CONTAINER_SELECTORS) {
                SelenideElement popup = $x(selector);
                if (popup.exists() && popup.isDisplayed()) {
                    logger.info("Found blocking popup, attempting to close it");
                    closeAnyVisiblePopup();
                    hasBlockingPopup = true;
                    break;
                }
            }
            
            if (!hasBlockingPopup) {
                // Check if page is interactive by trying to find main navigation
                if ($x("//nav").exists() || $x("//header").exists() || $x("//div[@role='navigation']").exists()) {
                    logger.info("Page appears to be interactive");
                    return;
                }
            }
            
            attempts++;
            sleep(1000); // Reduced sleep time
        }
        
        logger.warn("Page may not be fully interactive after {} attempts", maxAttempts);
    }
}
