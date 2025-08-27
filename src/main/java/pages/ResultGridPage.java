package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class ResultGridPage extends BasePage {
    
    // Locators
    private final SelenideElement supplierFilter = $x("//div[contains(text(),'Nhà sách Fahasa')]");
    private final ElementsCollection productPrices = $$x("//div[@class='price-discount__price']");
    
    @Step("Verify supplier filter is highlighted")
    public void verifySupplierFilterHighlighted() {
        logger.info("Verifying supplier filter is highlighted");
        verifyElementVisible(supplierFilter, "Supplier Filter");
    }
    
    @Step("Verify all product prices are within range: {minPrice} - {maxPrice}")
    public void verifyProductPricesInRange(int minPrice, int maxPrice) {
        logger.info("Verifying all product prices are within range: {} - {}", minPrice, maxPrice);
        
        for (SelenideElement priceElement : productPrices) {
            String priceText = priceElement.getText().replaceAll("[^0-9]", "");
            if (!priceText.isEmpty()) {
                int price = Integer.parseInt(priceText);
                logger.info("Product price: {}", price);
                assert price >= minPrice && price <= maxPrice : 
                    "Price " + price + " is not within range " + minPrice + " - " + maxPrice;
            }
        }
    }
}
