package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class FilterDialogPage extends BasePage {
    
    // Locators
    private final SelenideElement filterDialog = $x("//div[contains(text(),'Tất cả bộ lọc')]");
    private final SelenideElement fahasaSupplierCheckbox = $x("//label[contains(text(),'Nhà sách Fahasa')]//input[@type='checkbox']");
    private final SelenideElement minPriceInput = $x("//input[@placeholder='Từ']");
    private final SelenideElement maxPriceInput = $x("//input[@placeholder='Đến']");
    private final SelenideElement viewResultsButton = $x("//button[contains(text(),'Xem Kết quả')]");
    
    @Step("Verify filter dialog is displayed")
    public void verifyFilterDialogDisplayed() {
        verifyElementVisible(filterDialog, "Filter Dialog");
    }
    
    @Step("Select Fahasa supplier")
    public void selectFahasaSupplier() {
        logger.info("Selecting Fahasa supplier checkbox");
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
        logger.info("Clicking View Results button");
        clickElement(viewResultsButton, "View Results Button");
        return new ResultGridPage();
    }
}
