package com.softserve.edu14.modules;

import com.softserve.edu14.utils.ElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.function.Supplier;

import static com.softserve.edu14.test.TestsRunner.*;
import static com.softserve.edu14.utils.Utils.isUserCurrentlyLoggedIn;

public abstract class BasicFunctions {
    protected WebDriver driver;
    protected WebDriverWait wait;
    public BasicFunctions(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebElement getElement(By locator) {
        return handleWait(() -> driver.findElement(locator), locator);
    }

    public BasicFunctions inputText(By locator, String text) {
        WebElement element = getElement(locator);
        element.clear();
        element.sendKeys(text);
        return this;
    }

    public BasicFunctions clickElement(By locator) {
        waitElementToBeClickable(locator);
        getElement(locator).click();
        return this;
    }

    public WebElement waitElementToBePresent(By locator) {
        return handleWait(() -> wait.until(ExpectedConditions.presenceOfElementLocated(locator)), locator);
    }
    public WebElement waitElementToBeClickable(By locator) {
        return handleWait(() -> wait.until(ExpectedConditions.elementToBeClickable(locator)), locator);
    }

    public boolean waitElementToDisappear(By locator) {
        return handleWait(() -> wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)), locator);
    }

    private <T> T handleWait(Supplier<T> waitAction, By locator) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return waitAction.get();
        } catch (TimeoutException e) {
            throw new ElementNotFoundException(locator, driver, e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        }
    }

    public BasicFunctions openPage(String path) {
        driver.get(buildUrl(path));
        return this;
    }

    private String buildUrl(String path) {
        return BASE_URL + (path.startsWith("/") ? path : "/" + path);
    }

    public BasicFunctions switchLanguageToEnglish() {
        WebElement languageSwitcher = getElement(By.cssSelector("ul.header_lang-switcher-wrp"));
        if (languageSwitcher.getText().equalsIgnoreCase("ua")) {
            languageSwitcher.click();
            waitElementToBePresent(By.xpath("//li/span[text()='En']")).click();
        }
        return this;
    }

    public boolean validateUserLoggedIn() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".name")));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        return isUserCurrentlyLoggedIn();
    }
}
