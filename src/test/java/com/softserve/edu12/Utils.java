package com.softserve.edu12;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

import static com.softserve.edu12.TestsLaunchable.IMPLICITLY_WAIT_SECONDS;
import static com.softserve.edu12.TestsLaunchable.driver;

public class Utils {

    public static boolean isUserCurrentlyLoggedIn() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        boolean result = !driver.findElements(By.cssSelector(".name")).isEmpty()
                && driver.getCurrentUrl().contains("profile");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        return result;
    }

    public static void clearLocalStorage() {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        driver.navigate().refresh();
    }

}
