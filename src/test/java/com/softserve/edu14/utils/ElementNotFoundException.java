package com.softserve.edu14.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(By locator, WebDriver driver, Throwable cause) {
        super(buildMessage(locator, driver), cause);
    }

    private static String buildMessage(By locator, WebDriver driver) {
        return String.format("""
                Element not found:
                    Locator: [%s];
                    Page URL: [%s].
                """, locator.toString(), driver.getCurrentUrl()
        );
    }
}