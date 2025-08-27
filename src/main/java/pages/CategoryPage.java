package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class CategoryPage extends BasePage {
    
    // Locators
    private final ElementsCollection productItems = $$x("//div[@class='product-item']");
    
    @Step("Click on first product item")
    public ProductDetailPage clickFirstProduct() {
        logger.info("Clicking on first product item");
        if (!productItems.isEmpty()) {
            SelenideElement firstProduct = productItems.first();
            clickElement(firstProduct, "First Product");
            return new ProductDetailPage();
        }
        throw new RuntimeException("No products found on category page");
    }
    
    @Step("Get first product name")
    public String getFirstProductName() {
        if (!productItems.isEmpty()) {
            SelenideElement firstProduct = productItems.first();
            SelenideElement productName = firstProduct.$x(".//div[@class='name']");
            String name = productName.getText();
            logger.info("First product name: {}", name);
            return name;
        }
        throw new RuntimeException("No products found on category page");
    }
}
