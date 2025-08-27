package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CategoryPage;
import pages.ProductDetailPage;

@Feature("Recently Viewed Items")
public class ViewedItemsTest extends BaseTest {
    
    @Test
    @Story("Viewed item displays in recently viewed section")
    @Description("Verify viewed item displays in the 'Sản phẩm đã xem' section")
    public void testViewedItemsDisplay() {
        logger.info("Starting Test Case 002: Verify viewed item displays in recently viewed section");
        
        // Navigate to Sports category
        CategoryPage categoryPage = homePage.navigateToSportsCategory();
        
        // Get first product name and click on it
        String productName = categoryPage.getFirstProductName();
        ProductDetailPage productDetailPage = categoryPage.clickFirstProduct();
        
        // View product details (already on detail page)
        String viewedProductTitle = productDetailPage.getProductTitle();
        logger.info("Viewed product: {}", viewedProductTitle);
        
        // Go back to home page
        productDetailPage.goBack();
        homePage.navigateToSportsCategory(); // Navigate to any category
        
        // Verify viewed item displays in recently viewed section
        homePage.verifyRecentlyViewedSectionDisplayed();
        boolean isItemInRecentlyViewed = homePage.isItemInRecentlyViewed(productName);
        
        Assert.assertTrue(isItemInRecentlyViewed, 
            "Viewed item '" + productName + "' should be displayed in recently viewed section");
        
        logger.info("Test Case 002 completed successfully");
    }
}
