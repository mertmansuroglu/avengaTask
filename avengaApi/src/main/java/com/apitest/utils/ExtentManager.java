package com.apitest.utils;

import com.aventstack.extentreports.ExtentTest;

public class ExtentManager {
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    public static void setTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    public static void removeTest() {
        extentTestThreadLocal.remove();
    }
}
