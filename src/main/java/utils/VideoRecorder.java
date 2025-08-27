package utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Video recording utility for test automation
 * This class provides a framework for video recording during test execution.
 * 
 * To implement actual video recording, you can:
 * 1. Use Monte Screen Recorder library
 * 2. Integrate with Selenium Grid video recording
 * 3. Use FFmpeg wrapper libraries
 * 4. Use browser-specific recording capabilities
 * 
 * Current implementation provides the framework structure for video recording integration.
 */
public class VideoRecorder {
    private static final Logger logger = LoggerFactory.getLogger(VideoRecorder.class);
    private static final String VIDEOS_DIR = "target/videos";
    private static boolean isRecording = false;
    private static String currentVideoPath = null;

    static {
        createVideosDirectory();
    }

    /**
     * Start video recording for the current test
     * @param testName Name of the test case
     */
    public static void startRecording(String testName) {
        try {
            if (isRecording) {
                logger.warn("Video recording is already in progress");
                return;
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String videoFileName = String.format("%s_%s.mp4", testName, timestamp);
            currentVideoPath = Paths.get(VIDEOS_DIR, videoFileName).toString();

            // Note: Actual video recording implementation would depend on the tool used
            // For example, using Monte Screen Recorder, FFmpeg, or browser-specific tools
            logger.info("Video recording started for test: {} - {}", testName, currentVideoPath);
            isRecording = true;

        } catch (Exception e) {
            logger.error("Failed to start video recording: {}", e.getMessage(), e);
        }
    }

    /**
     * Stop video recording
     */
    public static void stopRecording() {
        try {
            if (!isRecording) {
                logger.warn("No video recording in progress");
                return;
            }

            // Stop recording logic would go here
            logger.info("Video recording stopped: {}", currentVideoPath);
            isRecording = false;

        } catch (Exception e) {
            logger.error("Failed to stop video recording: {}", e.getMessage(), e);
        }
    }

    /**
     * Stop recording and attach video to Allure report if test failed
     * @param testName Name of the test case
     * @param testFailed Whether the test failed
     */
    public static void stopAndAttachVideo(String testName, boolean testFailed) {
        stopRecording();

        if (testFailed && currentVideoPath != null) {
            attachVideoToAllure(testName, currentVideoPath);
        } else if (currentVideoPath != null) {
            // Delete video file if test passed (optional)
            deleteVideoFile(currentVideoPath);
        }

        currentVideoPath = null;
    }

    /**
     * Attach video file to Allure report
     * @param testName Name of the test case
     * @param videoPath Path to the video file
     */
    public static void attachVideoToAllure(String testName, String videoPath) {
        try {
            Path path = Paths.get(videoPath);
            if (Files.exists(path)) {
                byte[] videoBytes = Files.readAllBytes(path);
                Allure.addAttachment(
                    "Video Recording - " + testName,
                    "video/mp4",
                    new ByteArrayInputStream(videoBytes),
                    "mp4"
                );
                logger.info("Video attached to Allure report: {}", videoPath);
            } else {
                logger.warn("Video file not found for attachment: {}", videoPath);
            }
        } catch (IOException e) {
            logger.error("Failed to attach video to Allure report: {}", e.getMessage(), e);
        }
    }

    /**
     * Delete video file
     * @param videoPath Path to the video file
     */
    private static void deleteVideoFile(String videoPath) {
        try {
            Path path = Paths.get(videoPath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("Video file deleted: {}", videoPath);
            }
        } catch (IOException e) {
            logger.error("Failed to delete video file: {}", e.getMessage(), e);
        }
    }

    /**
     * Create videos directory if it doesn't exist
     */
    private static void createVideosDirectory() {
        try {
            Path path = Paths.get(VIDEOS_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created videos directory: {}", VIDEOS_DIR);
            }
        } catch (IOException e) {
            logger.error("Failed to create videos directory: {}", e.getMessage(), e);
        }
    }

    /**
     * Check if recording is in progress
     * @return true if recording is active
     */
    public static boolean isRecording() {
        return isRecording;
    }

    /**
     * Get current video path
     * @return path to current video file
     */
    public static String getCurrentVideoPath() {
        return currentVideoPath;
    }
}
