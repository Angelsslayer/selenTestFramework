package com.softserve.edu12;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Supplier;

import static com.softserve.edu12.TestsLaunchable.BASE_URL;
import static com.softserve.edu12.Utils.isUserCurrentlyLoggedIn;

public abstract class BasicFunctions {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final long IMPLICITLY_WAIT_SECONDS = 5L;

    public BasicFunctions(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebElement getElement(By locator) {
        return handleWait(() -> driver.findElement(locator), locator);
    }

    public void inputText(By locator, String text) {
        WebElement element = getElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clickElement(By locator) {
        getElement(locator).click();
    }

    public WebElement waitElementToBeVisible(By locator) {
        return handleWait(() -> wait.until(ExpectedConditions.presenceOfElementLocated(locator)), locator);
    }

    public boolean waitElementToDisappear(By locator) {
        return handleWait(() -> wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)), locator);
    }

    private <T> T handleWait(Supplier<T> waitAction, By locator) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return waitAction.get();
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Failed to find element: %s", locator));
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        }
    }

    public void openPage(String path) {
        driver.get(buildUrl(path));
    }

    private String buildUrl(String path) {
        return BASE_URL + (path.startsWith("/") ? path : "/" + path);
    }

    public void switchLanguageToEnglish() {
        WebElement languageSwitcher = getElement(By.cssSelector("ul.header_lang-switcher-wrp"));
        if (languageSwitcher.getText().equalsIgnoreCase("ua")) {
            languageSwitcher.click();
            waitElementToBeVisible(By.xpath("//li/span[text()='En']")).click();
        }
    }

    public boolean validateUserLoggedIn() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".name")));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        return isUserCurrentlyLoggedIn();
    }
}
