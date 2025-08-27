# Tiki Test Automation Framework

## Overview
This is a comprehensive test automation framework built with:
- **Selenide** 7.10.0 (latest)
- **Selenium WebDriver** 4.20.0 (latest)
- **Java 17**
- **TestNG** for test execution
- **Page Object Model** design pattern
- **Allure** reporting integration
- **Logback** for logging
- **JSON** data reading support

## Project Structure
```
src/
├── main/java/
│   ├── config/          # Configuration management
│   ├── constants/       # Test constants
│   ├── enums/          # Enums (BrowserType)
│   ├── models/         # Data models for JSON
│   ├── pages/          # Page Object classes
│   ├── utils/          # Utility classes
│   └── logger/         # Logging utilities
└── test/
    ├── java/tests/     # TestNG test classes
    └── resources/      # Test data, config files
```

## Configuration
- Browser configuration: `src/test/resources/config.properties`
- Test data: `src/test/resources/testdata.json`
- TestNG suite: `src/test/resources/testng.xml`

## Test Cases
1. **FilterSearchTest**: Verify user can filter search condition for products
2. **ViewedItemsTest**: Verify viewed items display in recently viewed section

## Running Tests
```bash
mvn clean test
```

## Generate Allure Report
```bash
mvn allure:report
mvn allure:serve
```
