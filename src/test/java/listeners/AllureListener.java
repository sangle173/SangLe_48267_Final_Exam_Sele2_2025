package listeners;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureListener extends AllureTestNg {
    private static final Logger logger = LoggerFactory.getLogger(AllureListener.class);
    private static final String SCREENSHOTS_DIR = "target/screenshots";
    private static final String VIDEOS_DIR = "target/videos";

    static {
        // Create directories if they don't exist
        createDirectoryIfNotExists(SCREENSHOTS_DIR);
        createDirectoryIfNotExists(VIDEOS_DIR);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        
        // Capture screenshot on failure
        captureScreenshot(result);
        
        // Attach browser logs
        attachBrowserLogs();
        
        // Attach page source
        attachPageSource();
        
        // Call parent method to ensure proper Allure integration
        super.onTestFailure(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
        
        // Capture screenshot for skipped tests as well
        captureScreenshot(result);
        
        super.onTestSkipped(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
        super.onTestSuccess(result);
    }

    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] captureScreenshot(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("%s_%s", testName, timestamp);
            
            // Take screenshot using Selenide
            String screenshotPath = Selenide.screenshot(fileName);
            
            if (screenshotPath != null) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    // Copy screenshot to our screenshots directory
                    Path targetPath = Paths.get(SCREENSHOTS_DIR, screenshotFile.getName());
                    Files.copy(screenshotFile.toPath(), targetPath);
                    
                    // Return screenshot bytes for Allure attachment
                    byte[] screenshotBytes = Files.readAllBytes(screenshotFile.toPath());
                    
                    // Also attach to Allure with custom name
                    Allure.addAttachment("Screenshot - " + testName, "image/png", 
                        new ByteArrayInputStream(screenshotBytes), "png");
                    
                    logger.info("Screenshot captured and attached: {}", fileName);
                    return screenshotBytes;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage(), e);
        }
        return new byte[0];
    }

    @Attachment(value = "Browser Logs", type = "text/plain")
    private String attachBrowserLogs() {
        try {
            StringBuilder logs = new StringBuilder();
            
            // Get browser console logs using proper Selenide API
            try {
                java.util.List<String> browserLogs = Selenide.getWebDriverLogs("browser");
                for (String logEntry : browserLogs) {
                    logs.append(logEntry).append("\n");
                }
            } catch (Exception e) {
                logs.append("Browser logs not available: ").append(e.getMessage()).append("\n");
            }
            
            String browserLogsContent = logs.toString();
            if (!browserLogsContent.isEmpty()) {
                logger.info("Browser logs attached to report");
                return browserLogsContent;
            }
            
            return "No browser logs available";
        } catch (Exception e) {
            logger.error("Failed to capture browser logs: {}", e.getMessage());
            return "Failed to capture browser logs: " + e.getMessage();
        }
    }

    @Attachment(value = "Page Source", type = "text/html")
    private String attachPageSource() {
        try {
            String pageSource = Selenide.webdriver().driver().getWebDriver().getPageSource();
            logger.info("Page source attached to report");
            return pageSource;
        } catch (Exception e) {
            logger.error("Failed to capture page source: {}", e.getMessage());
            return "<!-- Failed to capture page source: " + e.getMessage() + " -->";
        }
    }

    // Method to manually attach screenshot (can be called from test steps)
    public static void attachScreenshot(String name) {
        try {
            String screenshotPath = Selenide.screenshot(name);
            if (screenshotPath != null) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    byte[] screenshotBytes = Files.readAllBytes(screenshotFile.toPath());
                    Allure.addAttachment(name, "image/png", 
                        new ByteArrayInputStream(screenshotBytes), "png");
                    logger.info("Manual screenshot attached: {}", name);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to attach manual screenshot: {}", e.getMessage());
        }
    }

    // Method to attach custom text information
    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content, "txt");
        logger.info("Text attachment added: {}", name);
    }

    // Method to attach custom file
    public static void attachFile(String name, String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                byte[] fileBytes = Files.readAllBytes(path);
                String contentType = Files.probeContentType(path);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                
                Allure.addAttachment(name, contentType, 
                    new ByteArrayInputStream(fileBytes), 
                    path.getFileName().toString());
                logger.info("File attached: {}", filePath);
            } else {
                logger.warn("File not found for attachment: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to attach file: {}", e.getMessage());
        }
    }

    private static void createDirectoryIfNotExists(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created directory: {}", dirPath);
            }
        } catch (IOException e) {
            logger.error("Failed to create directory {}: {}", dirPath, e.getMessage());
        }
    }
}
