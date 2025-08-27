package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import constants.Constants;
import utils.PopupHandler;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BookStorePage extends BasePage {
    
    // Locators
    private final SelenideElement breadcrumb = $x("//nav[contains(@class,'breadcrumb')] | //div[contains(@class,'breadcrumb')] | //ol[contains(@class,'breadcrumb')]");
    
    @Step("Verify breadcrumb text")
    public void verifyBreadcrumb() {
        logger.info("Verifying breadcrumb: " + Constants.BREADCRUMB_BOOKSTORE);
        
        // Ensure no popups are blocking (only if visible)
        PopupHandler.closePopupIfVisible();
        
        // Wait for page to load properly
        waitForPageToLoad();
        
        verifyText(breadcrumb, Constants.BREADCRUMB_BOOKSTORE);
    }
    
    // Updated XPath for the "Tất cả" button based on the provided HTML structure
    private final SelenideElement allProductsButton = $x("//button[contains(@class,'sc-a04c7302-0') and .//div[text()='Tất cả']]");
    
    @Step("Click on All Products button")
    public FilterDialogPage clickAllProductsButton() {
        logger.info("Clicking on 'Tất cả' button to open filter dialog");
        
        // Log current URL before clicking
        String currentUrl = getWebDriver().getCurrentUrl();
        logger.info("Current URL before clicking: " + currentUrl);
        
        // Wait for the "Tất cả" button to be visible and clickable
        allProductsButton.shouldBe(visible).click();
        
        // Log URL after clicking to detect any unwanted redirects
        sleep(2000);
        String urlAfterClick = getWebDriver().getCurrentUrl();
        logger.info("URL after clicking 'Tất cả' button: " + urlAfterClick);
        
        // Check if we were redirected to an unexpected URL (like promotional links)
        if (urlAfterClick.contains("khuyen-mai") || urlAfterClick.contains("vinamilk") || urlAfterClick.contains("sua-duoi")) {
            logger.error("DETECTED UNEXPECTED REDIRECT TO PROMOTIONAL URL: " + urlAfterClick);
            // Navigate back to the correct book store page
            open("https://tiki.vn/nha-sach-tiki/c8322");
            sleep(2000);
            logger.info("Navigated back to book store page to avoid promotional content");
        }
        
        logger.info("Successfully clicked 'Tất cả' button");
        
        return new FilterDialogPage();
    }
}
