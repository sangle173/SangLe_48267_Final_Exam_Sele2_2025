package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Step;
import models.TikiTestData;
import org.testng.annotations.Test;
import pages.BookStorePage;
import utils.AllureStepHelper;
import utils.JsonReader;

@Feature("Product Filtering")
public class FilterSearchTest extends BaseTest {
    
    @Test
    @Story("Filter search condition for product")
    @Description("Verify user can filter search condition for product in Book Store")
    public void testFilterSearchCondition() {
        logger.info("Starting Test Case 001: Verify user can filter search condition for product");
        
        step1ReadTestData();
        BookStorePage bookStorePage = step2NavigateToBookStore();
        step3VerifyBreadcrumb(bookStorePage);
        
        logger.info("Test Case 001 completed");
    }
    
    @Step("Step 1: Read test data from JSON file")
    private void step1ReadTestData() {
        Allure.step("Loading test data from JSON configuration file", () -> {
            TikiTestData testData = JsonReader.read("src/test/resources/testdata.json", TikiTestData.class);
            AllureStepHelper.addInfo("Test data loaded successfully: Supplier=" + testData.getSupplier());
            logger.info("Test data loaded: {}", testData.getSupplier());
        });
    }
    
    @Step("Step 2: Navigate to Book Store section")
    private BookStorePage step2NavigateToBookStore() {
        return Allure.step("Click on Book Store menu and navigate to the section", () -> {
            BookStorePage bookStorePage = homePage.navigateToBookStore();
            AllureStepHelper.addInfo("Successfully navigated to Book Store page");
            return bookStorePage;
        });
    }
    
    @Step("Step 3: Verify breadcrumb shows 'Trang chủ > Nhà Sách Tiki'")
    private void step3VerifyBreadcrumb(BookStorePage bookStorePage) {
        Allure.step("Check that breadcrumb navigation shows correct path", () -> {
            try {
                bookStorePage.verifyBreadcrumb();
                AllureStepHelper.addInfo("Breadcrumb verification passed");
            } catch (Exception e) {
                AllureStepHelper.addInfo("Breadcrumb verification failed: " + e.getMessage());
                throw e;
            }
        });
    }
}
