package com.softserve.edu10;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SignInTest {

    private static final int WAIT_DURATION_SECONDS = 3;
    private static final Long IMPLICITLY_WAIT_SECONDS = 5L;
    private static final String BASE_URL = "http://localhost:4205/#/greenCity";
    private static WebDriver driver;
    private static WebDriverWait wait;
    private SignInPage signInPage;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_DURATION_SECONDS));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();
    }

    @BeforeEach
    public void init() {
        signInPage = new SignInPage(driver, wait);
        signInPage.openSignInPage(BASE_URL);
        ((JavascriptExecutor) driver).executeScript("window.localStorage.setItem('language', 'en');");
        driver.navigate().refresh();
        switchLanguageToEnglish();
        signInPage.clickSignInButton();
        waitElementToBeVisible(By.tagName("app-auth-modal"));
    }

    @Test
    public void testSignInFormUI() {
        Assertions.assertEquals("Welcome back!", signInPage.getWelcomeText());
        Assertions.assertEquals("Please enter your details to sign in.", signInPage.getSignInDetailsText());
        Assertions.assertTrue(signInPage.isForgotPasswordVisible(), "Forgot password link should be visible.");
        Assertions.assertTrue(signInPage.isSignUpLinkVisible(), "Sign-up link should be visible.");
        Assertions.assertTrue(signInPage.isGoogleSignInButtonVisible(), "Google Sign-In button should be visible.");
        Assertions.assertFalse(signInPage.isSubmitButtonEnabled(), "Sign In button should be disabled by default.");
    }

    @Test
    public void positiveSignInTest() {
        String testUserEmail = "validuser@greencity.com";
        String testUserPassword = "ValidPass123!";

        signInPage.inputCredentials(testUserEmail, testUserPassword);
        signInPage.clickSubmitButton();
        Assertions.assertDoesNotThrow(() -> waitElementToBeVisible(By.cssSelector(".name")));
        Assertions.assertTrue(driver.getCurrentUrl().contains("profile"), "URL does not contain 'profile'.");
        Assertions.assertEquals("ValidUser", driver.findElement(By.cssSelector(".name")).getText(),
                "User name does not match.");
    }

    @ParameterizedTest
    @CsvSource({
            "plainaddress",
            "@missingusername.com",
            "username@.missingdomain",
            "username@domain..com"
    })
    public void testInvalidEmailValidation(String invalidEmail) {
        signInPage.inputCredentials(invalidEmail, "ValidPass123!");
        signInPage.clickSubmitButton();
        Assertions.assertDoesNotThrow(() -> {
            signInPage.getErrorEmailText();
        }, "Error message is absent");
        String actualError = signInPage.getErrorEmailText();
        Assertions.assertEquals("Please check that your e-mail address is indicated correctly", actualError,
                String.format("Unexpected error message for email: %s", invalidEmail));
    }

    @Test
    public void testEmptyFieldsValidation() {
        signInPage.inputCredentials("", "");
        signInPage.clickSubmitButton();

        Assertions.assertEquals("Please fill all red fields", signInPage.getGeneralErrorText());
    }

    @ParameterizedTest
    @CsvSource({
            "validuser@greencity.com, alllowercase",
            "validuser@greencity.com, ALLUPPERCASE",
            "validuser@greencity.com, NoSpecial123"
    })
    public void testInvalidPasswordValidation(String email, String invalidPassword) {
        signInPage.inputCredentials(email, invalidPassword);
        signInPage.clickSubmitButton();
        Assertions.assertEquals("Bad email or password", signInPage.getGeneralErrorText());
    }

    @Test
    public void testPasswordLengthValidation() {
        signInPage.inputCredentials("validuser@greencity.com", "7chars!");
        signInPage.clickSubmitButton();

        Assertions.assertEquals("Password must be at least 8 characters long without spaces",
                signInPage.getErrorPasswordText());
    }

    @Test
    public void testCloseSignInWindow() {
        signInPage.closeModal();
        boolean actual = waitElementToDisappear(By.tagName("app-auth-modal"));
        Assertions.assertTrue(actual, "Modal window should be closed after clicking the close button.");
    }

    @Test
    public void showHidePasswordTest() {
        WebElement showHideButton = driver.findElement(By.cssSelector(".show-hide-btn"));

        showHideButton.click();
        Assertions.assertEquals("text", signInPage.passwordInput.getDomAttribute("type"),
                "Password field type should be 'text' after clicking 'show'.");

        showHideButton.click();
        Assertions.assertEquals("password", signInPage.passwordInput.getDomAttribute("type"),
                "Password field type should be 'password' after clicking 'hide'.");
    }

    @Test
    public void leftPasswordEmptyFieldTest() {
        signInPage.inputCredentials("validuser@greencity.com", "");
        signInPage.clickSubmitButton();

        String errorText = signInPage.getErrorPasswordText();
        Assertions.assertEquals("Password is required", errorText, "Error message for empty password is incorrect.");
    }

    @Test
    public void leftEmailEmptyFieldTest() {
        signInPage.inputCredentials("", "ValidPass123!");
        signInPage.clickSubmitButton();

        String errorText = signInPage.getErrorEmailText();
        Assertions.assertEquals("Email is required", errorText, "Error message for empty email is incorrect.");
    }

    @Test
    public void forgotPasswordIsClickableTest() {
        signInPage.forgotPassword.click();
        Assertions.assertTrue(driver.findElement(By.tagName("app-restore-password")).isDisplayed(),
                "Forgot Password window did not open.");
    }

    @Test
    public void signInButtonIsClickableTest() {
        signInPage.signUpLink.click();
        Assertions.assertTrue(driver.findElement(By.tagName("app-sign-up")).isDisplayed(),
                "Sign Up window did not open.");
    }

    @AfterEach
    public void clearResources() {
        List<WebElement> userProfile = driver.findElements(By.cssSelector("a.header_user-name"));
        if (!userProfile.isEmpty()) {
            logOut();
        }
        driver.manage().deleteAllCookies();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void switchLanguageToEnglish() {
        WebElement languageSwitcher = driver.findElement(By.cssSelector("ul.header_lang-switcher-wrp"));
        String currentLanguage = languageSwitcher.getText();
        if (currentLanguage.equalsIgnoreCase("ua")) {
            languageSwitcher.click();
            waitElementToBeClickable(By.xpath("//li/span[text()='En']")).click();
        }
    }

    private static WebElement waitElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private static void waitElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
    }

    private static boolean waitElementToDisappear(By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOf(driver.findElement(locator)));
        } catch (TimeoutException e) {
            return false;
        }
    }

    private static void logOut() {
        driver.findElement(By.cssSelector("a.header_user-name")).click();
        driver.findElement(By.cssSelector("li[aria-label='sign-out']")).click();
    }

}
