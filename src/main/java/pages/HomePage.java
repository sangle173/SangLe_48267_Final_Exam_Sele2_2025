package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import utils.PopupHandler;

import static com.codeborne.selenide.Selenide.*;

public class HomePage extends BasePage {
    
    // Locators
    private final SelenideElement sportsMenu = $x("//a[contains(text(),'Thể Thao - Dã Ngoại')]");
    private final SelenideElement recentlyViewedSection = $x("//div[contains(@class,'recently-viewed')]");
    
    @Step("Navigate to Book Store")
    public BookStorePage navigateToBookStore() {
        logger.info("Navigating to Book Store");
        
        // Navigate directly to the book store page to avoid clicking on promotional links
        open("https://tiki.vn/nha-sach-tiki/c8322");
        
        // Handle any popups that might appear after navigation (only if visible)
        PopupHandler.closePopupIfVisible();
        
        return new BookStorePage();
    }
    
    @Step("Navigate to Sports category")
    public CategoryPage navigateToSportsCategory() {
        logger.info("Navigating to Sports category");
        
        // Ensure page is interactive
        waitForPageToLoad();
        
        clickElement(sportsMenu, "Sports Menu");
        
        // Handle any popups after navigation (only if visible)
        PopupHandler.closePopupIfVisible();
        
        return new CategoryPage();
    }
    
    @Step("Verify recently viewed section is displayed")
    public void verifyRecentlyViewedSectionDisplayed() {
        // Ensure no popups are blocking the view (only if visible)
        PopupHandler.closePopupIfVisible();
        verifyElementVisible(recentlyViewedSection, "Recently Viewed Section");
    }
    
    @Step("Check if item is in recently viewed section: {itemName}")
    public boolean isItemInRecentlyViewed(String itemName) {
        logger.info("Checking if item '{}' is in recently viewed section", itemName);
        SelenideElement item = $x("//div[contains(@class,'recently-viewed')]//span[contains(text(),'" + itemName + "')]");
        return item.exists();
    }
}
