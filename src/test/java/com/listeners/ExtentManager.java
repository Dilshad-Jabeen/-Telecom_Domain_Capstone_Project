package com.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {

        if (extent == null) {

            ExtentSparkReporter spark =
                    new ExtentSparkReporter("test-output/ExtentReport.html");

            spark.config().setDocumentTitle("API - RestAssured Automation Report");
            spark.config().setReportName("API - Telecom Domain Capstone Project");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Tester", "Dilshad Jabeen");
            extent.setSystemInfo("Framework", "Rest Assured + TestNG");
            extent.setSystemInfo("Language", "Java");
        }

        return extent;
    }
}
