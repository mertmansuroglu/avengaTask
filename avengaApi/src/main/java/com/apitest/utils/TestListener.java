package com.apitest.utils;

import com.apitest.base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        try {
            Class existing = annotation.getRetryAnalyzerClass();
            if (existing == null) {
                annotation.setRetryAnalyzer(RetryAnalyzer.class);
            }
        } catch (Throwable ignored) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (result.getInstance() instanceof BaseTest) {
            ExtentTest t = com.apitest.utils.ExtentManager.getTest();
            if (t != null) t.info("Test started");
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest t = com.apitest.utils.ExtentManager.getTest();
        if (t != null) t.pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest t = com.apitest.utils.ExtentManager.getTest();
        if (t != null) {
            t.fail(result.getThrowable());
            Object respObj = result.getAttribute("response");
            if (respObj instanceof Response) {
                Response resp = (Response) respObj;
                t.info("Response status: " + resp.getStatusCode());
                t.info("Response body:\n" + resp.asPrettyString());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest t = com.apitest.utils.ExtentManager.getTest();
        if (t != null) t.skip("Test skipped: " + result.getThrowable());
    }

}