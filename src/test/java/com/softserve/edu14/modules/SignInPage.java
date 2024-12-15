package com.softserve.edu14.modules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignInPage extends BasicFunctions {
    private final By signInButton = By.cssSelector("a.header_sign-in-link");
    private final By welcomeText = By.cssSelector("app-sign-in>h1");
    private final By signInDetailsText = By.cssSelector("app-sign-in>h2");
    private final By signUpLink = By.cssSelector("app-sign-in p a[aria-label*='sign up']");
    private final By emailInput = By.id("email");
    private final By errorEmail = By.cssSelector("#email-err-msg app-error div");
    private final By passwordInput = By.id("password");
    private final By forgotPassword = By.cssSelector(".forgot-password");
    private final By showHideButton = By.cssSelector(".show-hide-btn");
    private final By errorPassword = By.cssSelector("#pass-err-msg app-error div");
    private final By signInSubmitButton = By.cssSelector("button.greenStyle");
    private final By errorGeneral = By.cssSelector(".alert-general-error");
    private final By closeSignInWindow = By.cssSelector(".close-modal-window");
    private final By googleSignInButton = By.cssSelector(".google-sign-in");


    public SignInPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public SignInPage openSignInPage(String path) {
        openPage(path);
        waitElementToBePresent(signInButton);
        return this;
    }

    public SignInPage clickSignInButton() {
        clickElement(signInButton);
        waitElementToBePresent(By.tagName("app-auth-modal"));
        return this;
    }

    public SignInPage closeModal() {
        clickElement(closeSignInWindow);
        return this;
    }

    public SignInPage inputCredentials(String email, String password) {
        inputText(emailInput, email);
        inputText(passwordInput, password);
        getElement(emailInput).click();
        return this;
    }

    public SignInPage clickSubmitButton() {
        clickElement(signInSubmitButton);
        return this;
    }

    public boolean isSubmitButtonClickable() {
        return getElement(signInSubmitButton).isEnabled();
    }

    public SignInPage clickForgotPassword() {
        clickElement(forgotPassword);
        return this;
    }

    public SignInPage clickSignUpLink() {
        clickElement(signUpLink);
        return this;
    }

    public boolean isSubmitButtonEnabled() {
        return getElement(signInSubmitButton).isEnabled();
    }

    public String getWelcomeText() {
        return getElement(welcomeText).getText();
    }

    public String getSignInDetailsText() {
        return getElement(signInDetailsText).getText();
    }

    public String getGeneralErrorText() {
        return waitElementToBePresent(errorGeneral).getText();
    }

    public String getErrorEmailText() {
        return waitElementToBePresent(errorEmail).getText();
    }

    public String getErrorPasswordText() {
        return waitElementToBePresent(errorPassword).getText();
    }

    public boolean isForgotPasswordVisible() {
        return getElement(forgotPassword).isDisplayed();
    }

    public boolean isSignUpLinkVisible() {
        return getElement(signUpLink).isDisplayed();
    }

    public boolean isGoogleSignInButtonVisible() {
        return getElement(googleSignInButton).isDisplayed();
    }

    public String getPasswordInputAttributeType() {
        return getElement(passwordInput).getDomAttribute("type");
    }

    public SignInPage clickShowHideButtonElement() {
        clickElement(showHideButton);
        return this;
    }

}
