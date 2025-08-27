package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PopupHandler;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public abstract class BasePage {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Step("Wait for page to load and close any popups")
    protected void waitForPageToLoad() {
        logger.info("Waiting for page to load and handling popups");
        sleep(1000); // Basic wait
        PopupHandler.closePopupIfVisible(); // Only close if popup is actually visible
    }
    
    @Step("Click element: {elementName}")
    protected void clickElement(SelenideElement element, String elementName) {
        logger.info("Clicking on: " + elementName);
        element.shouldBe(visible).click();
    }
    
    @Step("Enter text: {text} into {elementName}")
    protected void enterText(SelenideElement element, String text, String elementName) {
        logger.info("Entering text '{}' into: {}", text, elementName);
        element.shouldBe(visible).clear();
        element.setValue(text);
    }
    
    @Step("Verify element is visible: {elementName}")
    protected void verifyElementVisible(SelenideElement element, String elementName) {
        logger.info("Verifying element is visible: " + elementName);
        element.shouldBe(visible);
    }
    
    @Step("Verify text: {expectedText}")
    protected void verifyText(SelenideElement element, String expectedText) {
        logger.info("Verifying text: " + expectedText);
        element.shouldHave(text(expectedText));
    }
}
