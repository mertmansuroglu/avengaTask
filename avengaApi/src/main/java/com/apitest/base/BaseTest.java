package com.apitest.base;

import com.apitest.utils.ConfigManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import org.testng.annotations.*;

public class BaseTest {
    protected static ExtentReports extent;
    protected static ExtentTest test;
    
    @BeforeSuite
    public void beforeSuite() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/extent-report.html");
        extent.attachReporter(spark);
        
        extent.setSystemInfo("Environment", ConfigManager.getEnvironment());
        extent.setSystemInfo("Base URL", ConfigManager.getBaseUrl());
        extent.setSystemInfo("Username", ConfigManager.getUsername());
        
        RestAssured.baseURI = ConfigManager.getBaseUrl();
        com.apitest.base.BaseSpec.initializeToken();
    }
    
    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }
    
    @BeforeTest
    public void beforeTest() {
        System.out.println("Test class starting...");
    }
    
    @AfterTest
    public void afterTest() {
        System.out.println("Test class finished.");
    }
    
    @BeforeMethod
    public void beforeMethod(java.lang.reflect.Method method) {
        String displayName = getClass().getSimpleName() + "." + method.getName();
        String category = getCategoryFromClassName();
        test = extent.createTest(displayName).assignCategory(category);
        com.apitest.utils.ExtentManager.setTest(test);
    }
    
    private String getCategoryFromClassName() {
        String className = getClass().getSimpleName();
        if (className.contains("Books")) {
            return "Books API";
        } else if (className.contains("Authors")) {
            return "Authors API";
        }
        return "Other Tests";
    }
    
    @AfterMethod
    public void afterMethod() {
        com.apitest.utils.ExtentManager.removeTest();
    }
    
    protected void logInfo(String message) {
        test.info(message);
    }
    
    protected void logError(String message) {
        test.fail(message);
    }
}