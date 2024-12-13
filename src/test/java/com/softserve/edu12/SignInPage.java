package com.softserve.edu12;

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

    public void openSignInPage(String path) {
        openPage(path);
        waitElementToBeVisible(signInButton);
    }

    public void clickSignInButton() {
        clickElement(signInButton);
        waitElementToBeVisible(By.tagName("app-auth-modal"));
    }

    public void closeModal() {
        clickElement(closeSignInWindow);
    }

    public void inputCredentials(String email, String password) {
        inputText(emailInput, email);
        inputText(passwordInput, password);
        getElement(emailInput).click();
    }

    public void clickSubmitButton() {
        clickElement(signInSubmitButton);
    }

    public boolean isSubmitButtonClickable() {
        return getElement(signInSubmitButton).isEnabled();
    }

    public void clickForgotPassword() {
        clickElement(forgotPassword);
    }

    public void clickSignUpLink() {
        clickElement(signUpLink);
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
        return waitElementToBeVisible(errorGeneral).getText();
    }

    public String getErrorEmailText() {
        return waitElementToBeVisible(errorEmail).getText();
    }

    public String getErrorPasswordText() {
        return waitElementToBeVisible(errorPassword).getText();
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

    public void clickShowHideButtonElement() {
        clickElement(showHideButton);
    }

    public void switchToEnglishVersion() {
        switchLanguageToEnglish();
    }

    public boolean validateUserLoggedIn() {
        return super.validateUserLoggedIn();
    }

}
