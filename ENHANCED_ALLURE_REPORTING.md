# Enhanced Allure Reporting with Screenshots and Video Support

## 📋 Overview

This framework now includes comprehensive failure handling with automatic screenshot and video attachment capabilities for Allure reports. When tests fail, all relevant debugging information is automatically captured and attached to provide detailed failure analysis.

## 🎯 Features Implemented

### 1. Enhanced AllureListener (`listeners/AllureListener.java`)

**Automatic Failure Capture:**
- ✅ **Screenshots** - Automatically captured on test failure
- ✅ **Browser Logs** - Console logs attached for debugging
- ✅ **Page Source** - Complete HTML source at time of failure
- ✅ **Failure Reason** - Detailed error messages and stack traces

**Key Methods:**
```java
// Automatic attachments on test failure
@Override
public void onTestFailure(ITestResult result)

// Manual screenshot attachment
public static void attachScreenshot(String name)

// Text information attachment
public static void attachText(String name, String content)

// File attachment
public static void attachFile(String name, String filePath)
```

### 2. ScreenshotHelper Utility (`utils/ScreenshotHelper.java`)

**Screenshot Management:**
- ✅ **Automatic naming** with timestamps
- ✅ **Directory organization** in `target/screenshots/`
- ✅ **Allure integration** for report attachments
- ✅ **Error handling** for screenshot failures

**Usage Examples:**
```java
// Take screenshot with custom name
ScreenshotHelper.takeScreenshot("After Login");

// Attach text information
ScreenshotHelper.attachText("Test Data", "User: admin@test.com");

// Attach any file
ScreenshotHelper.attachFile("Config File", "config.properties");
```

### 3. Enhanced AllureStepHelper (`utils/AllureStepHelper.java`)

**Step Tracking with Screenshots:**
- ✅ **Regular steps** - Basic step tracking
- ✅ **Steps with screenshots** - Automatic screenshot after step
- ✅ **Critical steps** - Before/after screenshots for important actions
- ✅ **Navigation steps** - Screenshots after page navigation
- ✅ **Verification steps** - Screenshots after assertions

**Method Types:**
```java
// Basic step without screenshot
AllureStepHelper.step("Login to application", () -> {
    loginPage.login(username, password);
});

// Step with automatic screenshot
AllureStepHelper.stepWithScreenshot("Fill user details", () -> {
    userForm.fillDetails(userData);
});

// Critical step with before/after screenshots
AllureStepHelper.criticalStep("Submit payment", () -> {
    paymentPage.submitPayment();
});

// Navigation with screenshot
AllureStepHelper.navigationStep("Go to checkout", () -> {
    header.clickCheckout();
});

// Verification with screenshot
AllureStepHelper.verify("Order confirmation displayed", () -> {
    assertThat(orderPage.getConfirmationMessage()).isNotEmpty();
});
```

### 4. Video Recording Framework (`utils/VideoRecorder.java`)

**Video Recording Support:**
- ✅ **Framework structure** ready for video recording implementation
- ✅ **Failure-only recording** - Videos only attached when tests fail
- ✅ **Cleanup management** - Automatic deletion of videos from passed tests
- ✅ **Allure integration** - Videos attached to failure reports

**Implementation Notes:**
```java
// Framework methods available
VideoRecorder.startRecording(testName);
VideoRecorder.stopAndAttachVideo(testName, testFailed);
VideoRecorder.attachVideoToAllure(testName, videoPath);
```

**To Enable Actual Video Recording:**
Add one of these dependencies to `pom.xml`:
```xml
<!-- Option 1: Monte Screen Recorder -->
<dependency>
    <groupId>org.monte</groupId>
    <artifactId>monte-screen-recorder</artifactId>
    <version>0.7.7.0</version>
</dependency>

<!-- Option 2: TestNG Video Recorder -->
<dependency>
    <groupId>com.automation-remarks</groupId>
    <artifactId>video-recorder-testng</artifactId>
    <version>2.0</version>
</dependency>
```

### 5. Enhanced BaseTest (`tests/BaseTest.java`)

**Automatic Test Lifecycle Management:**
- ✅ **Setup screenshots** - Test start state capture
- ✅ **Teardown screenshots** - Test end state capture
- ✅ **Failure handling** - Enhanced debugging info for failures
- ✅ **Video integration** - Start/stop recording per test method

**Lifecycle Flow:**
```
Test Start → Start Video → Take Screenshot → Run Test → 
Test End → Take Screenshot → Stop Video → Attach if Failed
```

## 📁 File Organization

```
target/
├── screenshots/           # All test screenshots
│   ├── Test_Start_*.png
│   ├── Step_*.png
│   ├── Verification_*.png
│   └── Test_Failure_*.png
├── videos/               # Test failure videos (when implemented)
│   └── testName_*.mp4
├── allure-results/       # Allure test results
└── site/allure-maven-plugin/  # Generated Allure report
```

## 🚀 Usage in Tests

### Basic Test Implementation:
```java
@Test
public void testUserRegistration() {
    // Step 1: Navigate to registration
    AllureStepHelper.navigationStep("Open registration page", () -> {
        homePage.clickRegister();
    });
    
    // Step 2: Fill form with screenshot
    AllureStepHelper.stepWithScreenshot("Fill registration form", () -> {
        registrationPage.fillForm(testData);
    });
    
    // Step 3: Critical action with before/after screenshots
    AllureStepHelper.criticalStep("Submit registration", () -> {
        registrationPage.submit();
    });
    
    // Step 4: Verify with screenshot
    AllureStepHelper.verify("Registration success message", () -> {
        assertThat(registrationPage.getSuccessMessage())
            .contains("Registration successful");
    });
}
```

### Manual Screenshot Capture:
```java
// Take screenshot at any point
ScreenshotHelper.takeScreenshot("Custom checkpoint");

// Add information with screenshot
AllureStepHelper.addInfoWithScreenshot("Database state verified");

// Attach debugging information
ScreenshotHelper.attachText("API Response", jsonResponse);
```

## 📊 Allure Report Features

When you run `mvn allure:report` and open the report, you'll see:

### Test Details Page:
- ✅ **Step breakdown** with individual screenshots
- ✅ **Timeline** showing step execution order
- ✅ **Attachments section** with:
  - Screenshots at various test points
  - Browser console logs
  - Page source HTML
  - Custom text attachments
  - Video recordings (for failed tests)

### Failure Analysis:
- ✅ **Failure screenshot** - Visual state at time of failure
- ✅ **Browser logs** - Console errors and warnings
- ✅ **Page source** - Complete HTML for debugging
- ✅ **Error details** - Exception messages and stack traces
- ✅ **Video recording** - Complete test execution for failed tests

## ⚙️ Configuration

### Enable More Detailed Logging:
```properties
# config.properties
screenshot.on.failure=true
screenshot.on.success=false
video.recording.enabled=true
video.delete.on.success=true
browser.logs.enabled=true
```

### Selenide Configuration for Screenshots:
```properties
# Additional Selenide properties
selenide.screenshots=true
selenide.savePageSource=true
selenide.reportsFolder=target/screenshots
```

## 🔧 Customization

### Custom Screenshot Locations:
```java
// In your test methods
ScreenshotHelper.takeScreenshot("Before API call");
// API call here
ScreenshotHelper.takeScreenshot("After API call");
```

### Custom Attachments:
```java
// Attach test data
ScreenshotHelper.attachText("Test Input", testData.toString());

// Attach configuration files
ScreenshotHelper.attachFile("Environment Config", "config/test.properties");

// Attach API responses
ScreenshotHelper.attachText("API Response", response.body());
```

## 🎯 Benefits

1. **Enhanced Debugging** - Visual and textual failure analysis
2. **Detailed Test Steps** - Screenshot at each verification point
3. **Failure Investigation** - Complete context capture on failures
4. **Video Evidence** - Full test execution recording for failures
5. **Automatic Cleanup** - No storage waste from passed tests
6. **Team Collaboration** - Rich reports for shared debugging

## 📈 Best Practices

1. **Use appropriate step types** for different actions
2. **Take screenshots at critical points** in test flow
3. **Add contextual information** with text attachments
4. **Enable video recording only for CI/CD environments**
5. **Clean up media files** regularly to manage storage
6. **Use meaningful names** for screenshots and attachments

This enhanced framework provides comprehensive failure analysis capabilities while maintaining clean organization and automatic resource management.
