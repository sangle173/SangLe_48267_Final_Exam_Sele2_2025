package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import constants.Constants;
import utils.PopupHandler;

import static com.codeborne.selenide.Selenide.*;

public class BookStorePage extends BasePage {
    
    // Locators - more flexible selectors
    private final SelenideElement breadcrumb = $x("//nav[contains(@class,'breadcrumb')] | //div[contains(@class,'breadcrumb')] | //ol[contains(@class,'breadcrumb')]");
    private final SelenideElement allProductsButton = $x("//button[contains(text(),'Tất cả')] | //a[contains(text(),'Tất cả')]");
    
    @Step("Verify breadcrumb text")
    public void verifyBreadcrumb() {
        logger.info("Verifying breadcrumb: " + Constants.BREADCRUMB_BOOKSTORE);
        
        // Ensure no popups are blocking (only if visible)
        PopupHandler.closePopupIfVisible();
        
        // Wait for page to load properly
        waitForPageToLoad();
        
        verifyText(breadcrumb, Constants.BREADCRUMB_BOOKSTORE);
    }
    
    @Step("Click on All Products button")
    public FilterDialogPage clickAllProductsButton() {
        logger.info("Clicking on All Products button");
        
        // Ensure page is interactive (only close popup if visible)
        PopupHandler.closePopupIfVisible();
        
        clickElement(allProductsButton, "All Products Button");
        return new FilterDialogPage();
    }
}
