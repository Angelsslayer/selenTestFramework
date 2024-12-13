package com.softserve.edu12;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.softserve.edu12.Utils.clearLocalStorage;
import static com.softserve.edu12.Utils.isUserCurrentlyLoggedIn;

public interface TestsLaunchable {
    long IMPLICITLY_WAIT_SECONDS = 5L;
    long WAIT_DURATION_SECONDS = 3L;
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_DURATION_SECONDS));
    String BASE_URL = "http://localhost:4205/#";

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();
    }

    @BeforeEach
    default void init() {
        driver.manage().deleteAllCookies();
    }

    @AfterEach
    default void tearDownEach() {
        if (isUserCurrentlyLoggedIn()) {
            clearLocalStorage();
        }
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
