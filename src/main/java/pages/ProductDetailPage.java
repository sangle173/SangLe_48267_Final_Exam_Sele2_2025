package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class ProductDetailPage extends BasePage {
    
    // Locators
    private final SelenideElement productTitle = $x("//h1[@class='title']");
    private final SelenideElement backButton = $x("//button[contains(text(),'Quay lại')]");
    
    @Step("Get product title")
    public String getProductTitle() {
        String title = productTitle.getText();
        logger.info("Product title: {}", title);
        return title;
    }
    
    @Step("Go back to previous page")
    public void goBack() {
        logger.info("Going back to previous page");
        back();
    }
}
