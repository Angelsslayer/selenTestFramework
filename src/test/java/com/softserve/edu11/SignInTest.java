package com.softserve.edu11;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Stream;

public class SignInTest {
    private static final int WAIT_DURATION_SECONDS = 3;
    private static final long IMPLICITLY_WAIT_SECONDS = 5L;
    private static final String GREEN_CITY = "http://localhost:4205/#/greenCity";
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
        driver.manage().deleteAllCookies();
        signInPage = new SignInPage(driver, wait);
        signInPage.openPage(GREEN_CITY);
        switchLanguageToEnglish();
        signInPage.clickSignInButton();
    }

    @Test
    public void testSignInFormUI() {
        Assertions.assertEquals("Welcome back!", signInPage.getWelcomeText());
        Assertions.assertEquals("Please enter your details to sign in.", signInPage.getSignInDetailsText());
        Assertions.assertTrue(signInPage.isForgotPasswordVisible());
        Assertions.assertTrue(signInPage.isSignUpLinkVisible());
        Assertions.assertTrue(signInPage.isGoogleSignInButtonVisible());
        Assertions.assertFalse(signInPage.isSubmitButtonEnabled());
    }

    @Test
    public void testPositiveSignIn() {
        signInPage.inputCredentials("validuser@greencity.com", "ValidPass123!");
        signInPage.clickSubmitButton();
        Assertions.assertDoesNotThrow(() -> waitElementToBeVisible(By.cssSelector(".name")));
        Assertions.assertTrue(driver.getCurrentUrl().contains("profile"));
    }

    @Test
    public void testEmptyFieldsValidation() {
        signInPage.inputCredentials("", "");
        signInPage.clickSubmitButton();
        Assertions.assertEquals("Please fill all red fields", signInPage.getGeneralErrorText());
    }

    @Test
    public void testPasswordFieldEmpty() {
        signInPage.inputCredentials("validuser@greencity.com", "");
        signInPage.clickSubmitButton();
        Assertions.assertEquals("Password is required", signInPage.getErrorPasswordText());
    }

    @Test
    public void testEmailFieldEmpty() {
        signInPage.inputCredentials("", "ValidPass123!");
        signInPage.clickSubmitButton();
        Assertions.assertEquals("Email is required", signInPage.getErrorEmailText());
    }


    @ParameterizedTest(name = "[{index}] email: {0}, pass: {1} => expectedErrorMsg: {2}")
    @MethodSource("testEmailValidationNegative")
    public void testEmailValidationNegative(String email, String password, String expectedError) {
        signInPage.inputCredentials(email, password);
        signInPage.clickSubmitButton();
        Assertions.assertEquals(expectedError, signInPage.getErrorEmailText(), "Error message for Email fields was not found.");
    }

    static Stream<Arguments> testEmailValidationNegative() {
        String errorMessage = "Please check that your e-mail address is indicated correctly";
        String password = "ValidPass123!";
        return Stream.of(
                Arguments.of("plainaddress", password, errorMessage),
                Arguments.of("@missingusername.com", password, errorMessage),
                Arguments.of("username@.missingdomain", password, errorMessage)
        );
    }


    @ParameterizedTest(name = "[{index}] email: {0}, pass: {1} => expectedErrorMsg: {2}")
    @MethodSource()
    public void testPasswordValidationNegative(String email, String password, String expectedError) {
        signInPage.inputCredentials(email, password);
        signInPage.clickSubmitButton();
        Assertions.assertEquals(expectedError, signInPage.getErrorPasswordText(), "Error message for Password fields was not found.");
    }

    static Stream<Arguments> testPasswordValidationNegative() {
        String email = "validuser@greencity.com";
        String generalErrorMessage = "Password have from 8 to 20 characters long without spaces and contain at least one uppercase letter (A-Z), one lowercase letter (a-z), a digit (0-9), and a special character (~`!@#$%^&*()+=_-{}[]|:;”’?/<>,.)";
        return Stream.of(
                Arguments.of(email, "7Chars!", "Password must be at least 8 characters long without spaces"),
                Arguments.of(email, "with whiteSpace1!", generalErrorMessage),
                Arguments.of(email, "21CharactersLongPass!", "Password must be less than 20 characters long without spaces."),
                Arguments.of(email, "NoSpecialChar1", generalErrorMessage),
                Arguments.of(email, "NoDigitChar!", generalErrorMessage),
                Arguments.of(email, "no_upper_case_char1", generalErrorMessage)
        );
    }


    @Test
    public void testCloseSignInWindow() {
        signInPage.closeModal();
        Assertions.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("app-auth-modal"))));
    }

    @Test
    public void testShowHidePassword() {
        signInPage.getShowHideButton().click();
        Assertions.assertEquals("text", signInPage.getPasswordInput().getDomAttribute("type"));

        signInPage.getShowHideButton().click();
        Assertions.assertEquals("password", signInPage.getPasswordInput().getDomAttribute("type"));
    }

    @Test
    public void testForgotPasswordIsClickable() {
        signInPage.getForgotPassword().click();
        Assertions.assertTrue(driver.findElement(By.tagName("app-restore-password")).isDisplayed());
    }

    @Test
    public void testSignInButtonIsClickable() {
        signInPage.getSignUpLink().click();
        Assertions.assertTrue(driver.findElement(By.tagName("app-sign-up")).isDisplayed());
    }

    @AfterEach
    public void clearResources() {
        clearLocalStorage();
        driver.navigate().refresh();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void switchLanguageToEnglish() {
        WebElement languageSwitcher = driver.findElement(By.cssSelector("ul.header_lang-switcher-wrp"));
        if (languageSwitcher.getText().equalsIgnoreCase("ua")) {
            languageSwitcher.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li/span[text()='En']"))).click();
        }
    }

    public static WebElement waitElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public static WebElement waitElementToBeVisible(WebElement webElement) {
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }
    public static void clearLocalStorage() {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
    }
}