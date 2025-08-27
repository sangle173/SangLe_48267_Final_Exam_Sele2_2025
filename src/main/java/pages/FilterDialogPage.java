package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class FilterDialogPage extends BasePage {
    
    // Locators - more flexible for different filter layouts
    private final SelenideElement filterDialog = $x("//div[contains(text(),'Tất cả bộ lọc')] | //div[contains(text(),'Bộ lọc')] | //div[contains(@class,'filter')]");
    private final SelenideElement supplierSection = $x("//h4[contains(text(),'Nhà cung cấp')] | //div[contains(text(),'Nhà cung cấp')] | //span[contains(text(),'Nhà cung cấp')]");
    private final SelenideElement viewMoreSuppliersButton = $x("//div[@data-view-label='Nhà cung cấp']//a[contains(@class,'toggler') and contains(text(),'Xem thêm')] | //a[contains(text(),'Xem thêm')] | //button[contains(text(),'Xem thêm')]");
    private final SelenideElement fahasaSupplierCheckbox = $x("//label[contains(@class,'item--seller')]//span[contains(text(),'Nhà sách Fahasa')] | //span[contains(text(),'Nhà sách Fahasa')] | //div[contains(text(),'Nhà sách Fahasa')]");
    private final SelenideElement minPriceInput = $x("//input[@placeholder='Từ'] | //input[contains(@placeholder,'giá')] | //input[contains(@class,'price')] | //input[@type='number']");
    private final SelenideElement maxPriceInput = $x("//input[@placeholder='Đến'] | //input[contains(@placeholder,'giá')] | //input[contains(@class,'price')]");
    private final SelenideElement viewResultsButton = $x("//button[contains(text(),'Xem kết quả')] | //button[contains(text(),'Xem Kết quả')] | //button[contains(text(),'Xem Kết Quả')] | //button[contains(@class,'sc-') and contains(text(),'Xem')]");
    
    @Step("Verify filter dialog is displayed")
    public void verifyFilterDialogDisplayed() {
        // First, try to open the filter panel if it's not already open
        try {
            // Look for a filter button or panel toggle that doesn't involve promotional content
            SelenideElement filterToggle = $x("//div[contains(@class,'filter')] | //button[contains(@class,'filter')] | //span[contains(text(),'Lọc')]");
            if (filterToggle.exists()) {
                logger.info("Found filter toggle, scrolling to it and clicking to open filter panel");
                // Scroll to the element first to ensure it's not covered
                filterToggle.scrollTo();
                sleep(500);
                // Use JavaScript click to avoid click interception
                executeJavaScript("arguments[0].click();", filterToggle);
                sleep(1000);
            }
        } catch (Exception e) {
            logger.info("Filter panel might already be open or accessible: " + e.getMessage());
        }
        
        // Now verify the filter dialog or filter elements are accessible
        try {
            verifyElementVisible(filterDialog, "Filter Dialog");
        } catch (Exception e) {
            logger.info("Main filter dialog not found, checking for filter sections directly");
            // If the main dialog isn't found, check if supplier section is available
            verifyElementVisible(supplierSection, "Supplier Filter Section");
        }
    }
    
    @Step("Select Fahasa supplier")
    public void selectFahasaSupplier() {
        logger.info("Scrolling to supplier section and expanding supplier list");
        
        // Scroll to supplier section
        supplierSection.scrollTo();
        
        // Click "Xem thêm" to expand the supplier list
        logger.info("Clicking 'Xem thêm' to show more suppliers");
        clickElement(viewMoreSuppliersButton, "View More Suppliers Button");
        
        // Wait a moment for the expansion
        sleep(1000);
        
        // Select Fahasa supplier
        logger.info("Selecting Nhà sách Fahasa supplier");
        clickElement(fahasaSupplierCheckbox, "Fahasa Supplier Checkbox");
    }
    
    @Step("Enter price range: {minPrice} - {maxPrice}")
    public void enterPriceRange(int minPrice, int maxPrice) {
        logger.info("Entering price range: {} - {}", minPrice, maxPrice);
        enterText(minPriceInput, String.valueOf(minPrice), "Min Price Input");
        enterText(maxPriceInput, String.valueOf(maxPrice), "Max Price Input");
    }
    
    @Step("Click View Results button")
    public ResultGridPage clickViewResultsButton() {
        logger.info("Looking for View Results button");
        
        // Try to scroll to the bottom to make sure the button is visible
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
        sleep(2000);
        
        // Try multiple strategies to find and click the button
        try {
            // Strategy 1: Use the exact CSS class from the HTML you provided
            SelenideElement button1 = $(".sc-add2a4bc-6.UkSzZ");
            if (button1.exists()) {
                logger.info("Found 'Xem kết quả' button using CSS class selector");
                // Use JavaScript click since it's not a traditional button
                executeJavaScript("arguments[0].click();", button1);
                logger.info("Successfully clicked 'Xem kết quả' button using JavaScript click");
                return new ResultGridPage();
            }
        } catch (Exception e) {
            logger.debug("Strategy 1 (CSS + JS) failed: " + e.getMessage());
        }
        
        try {
            // Strategy 2: Direct text search with JavaScript click
            SelenideElement button2 = $x("//div[contains(text(), 'Xem kết quả')]");
            if (button2.exists()) {
                logger.info("Found 'Xem kết quả' button using text search");
                executeJavaScript("arguments[0].click();", button2);
                logger.info("Successfully clicked 'Xem kết quả' button using text search + JavaScript");
                return new ResultGridPage();
            }
        } catch (Exception e) {
            logger.debug("Strategy 2 (Text + JS) failed: " + e.getMessage());
        }
        
        try {
            // Strategy 3: Try original approach as fallback
            logger.info("Trying original selector for View Results button");
            clickElement(viewResultsButton, "View Results Button");
            return new ResultGridPage();
        } catch (Exception e) {
            logger.debug("Strategy 3 (Original) failed: " + e.getMessage());
        }
        
        throw new RuntimeException("Failed to find and click 'Xem kết quả' button using any strategy");
    }
}
