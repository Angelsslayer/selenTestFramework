package com.softserve.edu14.test;

import com.softserve.edu14.utils.ConfigLoader;
import com.softserve.edu14.utils.TestResultStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.softserve.edu14.utils.Utils.*;

@ExtendWith(TestResultStatus.class)
public interface TestsRunner {
    long IMPLICIT_WAIT_SECONDS = ConfigLoader.getLongProperty("IMPLICIT_WAIT_SECONDS");
    long EXPLICIT_WAIT_SECONDS = ConfigLoader.getLongProperty("EXPLICIT_WAIT_SECONDS");
    String BASE_URL = ConfigLoader.getProperty("BASE_URL");
    ThreadLocal<Boolean> isTestFailed = ThreadLocal.withInitial(() -> false);

    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        driver.manage().window().maximize();
    }

    @BeforeEach
    default void init() {
        driver.manage().deleteAllCookies();
    }

    @AfterEach
    default void tearDownEach(TestInfo testInfo) {
        if (isUserCurrentlyLoggedIn()) {
            clearLocalStorage();
        }

        if (isTestFailed.get()) {
            System.out.println(prettifyTestInfo(testInfo));
        }
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
