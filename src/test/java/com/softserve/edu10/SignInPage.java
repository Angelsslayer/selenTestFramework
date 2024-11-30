package com.softserve.edu10;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignInPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "a.header_sign-in-link")
    private WebElement signInButton;

    @FindBy(css = "app-sign-in>h1")
    private WebElement welcomeText;

    @FindBy(css = "app-sign-in>h2")
    private WebElement signInDetailsText;

    @FindBy(css = ".forgot-password")
    WebElement forgotPassword;

    @FindBy(css = "app-sign-in p a[aria-label*='sign up']")
    WebElement signUpLink;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    WebElement passwordInput;

    @FindBy(css = "button.greenStyle")
    private WebElement signInSubmitButton;

    @FindBy(css = ".alert-general-error")
    private WebElement errorGeneral;

    @FindBy(css = "#email-err-msg app-error div")
    private WebElement errorEmail;

    @FindBy(css = "#pass-err-msg app-error div")
    private WebElement errorPassword;

    @FindBy(css = ".close-modal-window")
    private WebElement closeSignInWindow;

    @FindBy(css = ".google-sign-in")
    private WebElement googleSignInButton;

    public SignInPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void openSignInPage(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOf(signInButton));
    }

    public void clickSignInButton() {
        signInButton.click();
    }

    public void closeModal() {
        closeSignInWindow.click();
    }

    public void inputCredentials(String email, String password) {
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickSubmitButton() {
        signInSubmitButton.click();
    }

    public boolean isSubmitButtonEnabled() {
        return signInSubmitButton.isEnabled();
    }

    public String getWelcomeText() {
        return welcomeText.getText();
    }

    public String getSignInDetailsText() {
        return signInDetailsText.getText();
    }

    public String getGeneralErrorText() {
        return wait.until(ExpectedConditions.visibilityOf(errorGeneral)).getText();
    }
    public String getErrorEmailText() {
        return wait.until(ExpectedConditions.visibilityOf(errorEmail)).getText();
    }

    public String getErrorPasswordText() {
        return wait.until(ExpectedConditions.visibilityOf(errorPassword)).getText();
    }

    public boolean isForgotPasswordVisible() {
        return forgotPassword.isDisplayed();
    }

    public boolean isSignUpLinkVisible() {
        return signUpLink.isDisplayed();
    }

    public boolean isGoogleSignInButtonVisible() {
        return googleSignInButton.isDisplayed();
    }
}
