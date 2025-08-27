package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.TikiTestData;
import org.testng.annotations.Test;
import pages.BookStorePage;
import utils.JsonReader;

import static org.testng.Assert.assertTrue;

@Feature("Demonstration Tests")
public class FailureScreenshotDemoTest extends BaseTest {
    
    @Test
    @Story("Demonstrate automatic failure screenshot")
    @Description("This test intentionally fails to demonstrate automatic screenshot capture on failure")
    public void testFailureScreenshotDemo() {
        logger.info("Starting Demo Test: Demonstrate automatic failure screenshot");
        
        // Step 1: Load test data
        Allure.step("Step 1: Load test data", () -> {
            TikiTestData data = JsonReader.read("src/test/resources/testdata.json", TikiTestData.class);
            logger.info("Test data loaded: {}", data.getSupplier());
        });
        
        // Step 2: Navigate to Book Store
        BookStorePage bookStorePage = Allure.step("Step 2: Navigate to Book Store", () -> {
            BookStorePage page = homePage.navigateToBookStore();
            logger.info("Successfully navigated to Book Store page");
            return page;
        });
        
        // Step 3: Verify breadcrumb (this will pass)
        Allure.step("Step 3: Verify breadcrumb", () -> {
            bookStorePage.verifyBreadcrumb();
            logger.info("Breadcrumb verification passed");
        });
        
        // Step 4: Intentional failure to demonstrate automatic screenshot
        Allure.step("Step 4: Intentional failure for demo", () -> {
            logger.info("About to perform intentional failure to demonstrate automatic screenshot capture");
            
            // This assertion will fail intentionally
            assertTrue(false, "This is an intentional failure to demonstrate automatic screenshot capture on test failure. " +
                              "The AllureListener will automatically capture a screenshot, browser logs, and page source.");
        });
        
        logger.info("Demo test completed (this won't be reached due to failure)");
    }
}
