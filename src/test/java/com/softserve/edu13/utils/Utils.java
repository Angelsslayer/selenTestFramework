package com.softserve.edu13.utils;

import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.lang.reflect.Method;
import java.time.Duration;

import static com.softserve.edu13.test.TestsRunner.IMPLICIT_WAIT_SECONDS;
import static com.softserve.edu13.test.TestsRunner.driver;


public class Utils {

    public static boolean isUserCurrentlyLoggedIn() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        boolean result = !driver.findElements(By.cssSelector(".name")).isEmpty()
                && driver.getCurrentUrl().contains("profile");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        return result;
    }

    public static void clearLocalStorage() {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        driver.navigate().refresh();
    }

    public static String prettifyTestInfo(TestInfo testInfo) {
        String testMethodName = testInfo.getTestMethod()
                .map(Method::getName)
                .orElse("Error occurred while getting info from TestInfo -> getTestMethod()");
        String testDisplayName = testInfo.getDisplayName();
        String testClassName = testInfo.getTestClass()
                .map(Class::getSimpleName)
                .orElse("Error occurred while getting info from TestInfo -> getTestClass()");

        return String.format("""
                Details about failed test:
                    Test Method: %s;
                    Test Method name: %s;
                    Test Class: %s.
                """, testMethodName, testDisplayName, testClassName);
    }

}
