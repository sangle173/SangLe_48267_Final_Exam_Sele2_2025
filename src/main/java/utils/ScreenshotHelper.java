package utils;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot utility for Allure reporting
 */
public class ScreenshotHelper {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotHelper.class);
    private static final String SCREENSHOTS_DIR = "target/screenshots";

    static {
        createScreenshotsDirectory();
    }

    /**
     * Take screenshot and attach to Allure report
     * @param name Name for the screenshot
     */
    public static void takeScreenshot(String name) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("%s_%s", name.replaceAll("[^a-zA-Z0-9]", "_"), timestamp);
            
            String screenshotPath = Selenide.screenshot(fileName);
            
            if (screenshotPath != null) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    // Copy to screenshots directory
                    Path targetPath = Paths.get(SCREENSHOTS_DIR, screenshotFile.getName());
                    Files.copy(screenshotFile.toPath(), targetPath);
                    
                    // Attach to Allure
                    byte[] screenshotBytes = Files.readAllBytes(screenshotFile.toPath());
                    Allure.addAttachment(name, "image/png", 
                        new ByteArrayInputStream(screenshotBytes), "png");
                    
                    logger.info("Screenshot taken and attached: {}", name);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot: {}", e.getMessage(), e);
        }
    }

    /**
     * Attach text information to Allure report
     * @param name Name for the attachment
     * @param content Text content
     */
    public static void attachText(String name, String content) {
        try {
            Allure.addAttachment(name, "text/plain", content, "txt");
            logger.info("Text attachment added: {}", name);
        } catch (Exception e) {
            logger.error("Failed to attach text: {}", e.getMessage(), e);
        }
    }

    /**
     * Attach file to Allure report
     * @param name Name for the attachment
     * @param filePath Path to the file
     */
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
            logger.error("Failed to attach file: {}", e.getMessage(), e);
        }
    }

    /**
     * Create screenshots directory if it doesn't exist
     */
    private static void createScreenshotsDirectory() {
        try {
            Path path = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created screenshots directory: {}", SCREENSHOTS_DIR);
            }
        } catch (IOException e) {
            logger.error("Failed to create screenshots directory: {}", e.getMessage(), e);
        }
    }
}
