package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.ScreenshotHelper;

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
        
        // Scroll to top of the page to ensure we see all products from the beginning
        logger.info("Scrolling to top of page before price verification");
        executeJavaScript("window.scrollTo(0, 0);");
        sleep(1000); // Wait for scroll to complete
        
        // Wait for filter results to load completely
        logger.info("Waiting for filtered results to load completely");
        sleep(3000);
        
        // First capture a screenshot before verification
        captureScreenshotWithStep("Before Price Verification - Product Results Page");
        
        boolean hasValidProducts = false;
        StringBuilder priceDetails = new StringBuilder("Price verification details:\n");
        
        for (SelenideElement priceElement : productPrices) {
            String priceText = priceElement.getText().replaceAll("[^0-9]", "");
            if (!priceText.isEmpty()) {
                hasValidProducts = true;
                int price = Integer.parseInt(priceText);
                logger.info("Product price: {}", price);
                priceDetails.append("Product price: ").append(price).append("\n");
                
                if (price < minPrice || price > maxPrice) {
                    // Capture screenshot when price verification fails
                    captureScreenshotWithStep("Price Verification Failed - Price Outside Range");
                    
                    String failureDetails = priceDetails.toString() + 
                        "FAILED: Price " + price + " is not within range " + minPrice + " - " + maxPrice;
                    ScreenshotHelper.attachText("Price Verification Details", failureDetails);
                    
                    throw new AssertionError("Price " + price + " is not within range " + minPrice + " - " + maxPrice);
                }
            }
        }
        
        if (!hasValidProducts) {
            captureScreenshotWithStep("No Valid Products Found");
            ScreenshotHelper.attachText("Price Verification Issue", "No valid product prices found on the page");
            throw new AssertionError("No valid product prices found on the page");
        }
        
        // If we reach here, all prices are valid
        captureScreenshotWithStep("Price Verification Successful - All Prices In Range");
        ScreenshotHelper.attachText("Price Verification Success", 
            priceDetails.toString() + "SUCCESS: All prices are within range " + minPrice + " - " + maxPrice);
    }
    
    @Step("Screenshot: {description}")
    private void captureScreenshotWithStep(String description) {
        try {
            // Use direct Allure attachment annotation approach
            takeScreenshotAsBytes();
            logger.info("Screenshot captured for: {}", description);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }
    
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] takeScreenshotAsBytes() {
        try {
            // Take screenshot using Selenide's WebDriver directly
            return ((TakesScreenshot) WebDriverRunner.getWebDriver())
                .getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to take screenshot bytes: {}", e.getMessage());
            // Fallback: try with file approach
            try {
                String screenshotPath = Selenide.screenshot("fallback_screenshot");
                if (screenshotPath != null) {
                    java.io.File screenshotFile = new java.io.File(screenshotPath);
                    if (screenshotFile.exists()) {
                        return java.nio.file.Files.readAllBytes(screenshotFile.toPath());
                    }
                }
            } catch (Exception fallbackException) {
                logger.error("Fallback screenshot also failed: {}", fallbackException.getMessage());
            }
            return new byte[0];
        }
    }
}
