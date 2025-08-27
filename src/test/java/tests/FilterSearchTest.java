package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.TikiTestData;
import org.testng.annotations.Test;
import pages.BookStorePage;
import pages.FilterDialogPage;
import pages.ResultGridPage;
import utils.JsonReader;

@Feature("Product Filtering")
public class FilterSearchTest extends BaseTest {
    
    @Test
    @Story("Filter search condition for product")
    @Description("Test Case 001: Verify user can filter search condition for product - Complete implementation with price range and supplier filter")
    public void testFilterSearchCondition() {
        logger.info("Starting Test Case 001: Verify user can filter search condition for product");
        
        // Step 1: Read test data from JSON file (for future use)
        Allure.step("Step 1: Load test data from JSON configuration", () -> {
            TikiTestData data = JsonReader.read("src/test/resources/testdata.json", TikiTestData.class);
            logger.info("Test data loaded: Supplier={}", data.getSupplier());
        });
        
        // Step 2: Navigate to Book Store section (Nhà Sách Tiki)
        BookStorePage bookStorePage = Allure.step("Step 2: Navigate to Book Store section", () -> {
            return homePage.navigateToBookStore();
        });
        
        // Step 3: Verify breadcrumb shows 'Trang chủ > Nhà Sách Tiki'
        Allure.step("Step 3: Verify breadcrumb shows 'Trang chủ > Nhà Sách Tiki'", () -> {
            bookStorePage.verifyBreadcrumb();
        });
        
        // Step 4: Click on "Tất cả" button under "Tất cả sản phẩm" section
        FilterDialogPage filterDialogPage = Allure.step("Step 4: Click on 'Tất cả' button under 'Tất cả sản phẩm' section", () -> {
            return bookStorePage.clickAllProductsButton();
        });
        
        // Step 5: Verify "Tất cả bộ lọc" dialog is displayed
        Allure.step("Step 5: Verify 'Tất cả bộ lọc' dialog is displayed", () -> {
            filterDialogPage.verifyFilterDialogDisplayed();
        });
        
        // Step 6: Check on "Nhà cung cấp Nhà sách Fahasa" checkbox
        Allure.step("Step 6: Select 'Nhà sách Fahasa' supplier checkbox", () -> {
            filterDialogPage.selectFahasaSupplier();
        });
        
        // Step 7: Enter price range 60.000 - 140.000 and click "Xem Kết quả"
        ResultGridPage resultGridPage = Allure.step("Step 7: Enter price range 60.000 - 140.000 and click 'Xem Kết quả'", () -> {
            int minPrice = 60000;
            int maxPrice = 140000;
            filterDialogPage.enterPriceRange(minPrice, maxPrice);
            return filterDialogPage.clickViewResultsButton();
        });
        
        // Step 8: Verify supplier filter is highlighted
        Allure.step("Step 8: Verify 'Nhà sách Fahasa' supplier is highlighted", () -> {
            resultGridPage.verifySupplierFilterHighlighted();
        });
        
        // Step 9: Verify all product prices are within the specified range
        Allure.step("Step 9: Verify all product prices are within range 60.000đ - 140.000đ", () -> {
            int minPrice = 60000;
            int maxPrice = 140000;
            resultGridPage.verifyProductPricesInRange(minPrice, maxPrice);
        });
        
        logger.info("Test Case 001 completed successfully - All filter conditions verified");
    }
}
