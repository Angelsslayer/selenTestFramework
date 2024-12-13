package com.softserve.edu12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignInTest implements TestsLaunchable {
    private SignInPage signInPage;

    @BeforeEach
    public void init() {
        TestsLaunchable.super.init();
        signInPage = new SignInPage(driver, wait);
        signInPage.openSignInPage("/greenCity");
        signInPage.switchToEnglishVersion();
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

    @ParameterizedTest
    @MethodSource()
    public void testPositiveSignIn(String email, String password) {
        signInPage.inputCredentials(email, password);
        signInPage.clickSubmitButton();
        Assertions.assertTrue(signInPage.validateUserLoggedIn());
    }

    private static Stream<Arguments> testPositiveSignIn() {
        return Stream.of(
                Arguments.of("validuser@greencity.com", "ValidPass123!")
        );
    }

    @ParameterizedTest
    @MethodSource()
    public void testEmptyFieldsValidation(String email, String password, String errorText) {
        signInPage.inputCredentials(email, password);
        Assertions.assertFalse(signInPage.isSubmitButtonClickable());
        Assertions.assertEquals(errorText, signInPage.getGeneralErrorText());
    }

    private static Stream<Arguments> testEmptyFieldsValidation() {
        return Stream.of(
                Arguments.of("", "", "Please fill all red fields")
        );
    }

    @ParameterizedTest
    @MethodSource()
    public void testPasswordFieldEmpty(String email, String password, String errorText) {
        signInPage.inputCredentials(email, password);

        signInPage.clickSubmitButton();
        Assertions.assertEquals(errorText, signInPage.getErrorPasswordText());
    }

    private static Stream<Arguments> testPasswordFieldEmpty() {
        return Stream.of(
                Arguments.of("validuser@greencity.com", "", "Password is required")
        );
    }

    @ParameterizedTest
    @MethodSource()
    public void testIncorrectPasswordField(String email, String password, String errorText) {
        signInPage.inputCredentials(email, password);

        signInPage.clickSubmitButton();
        Assertions.assertEquals(errorText, signInPage.getGeneralErrorText());
    }

    private static Stream<Arguments> testIncorrectPasswordField() {
        return Stream.of(
                Arguments.of("validuser@greencity.com", "IncorrectPass1!", "Bad email or password")
        );
    }

    @ParameterizedTest
    @MethodSource()
    public void testEmailFieldEmpty(String email, String password, String errorText) {
        signInPage.inputCredentials(email, password);
        signInPage.clickSubmitButton();
        Assertions.assertEquals(errorText, signInPage.getErrorEmailText());
    }

    private static Stream<Arguments> testEmailFieldEmpty() {
        return Stream.of(
                Arguments.of("", "ValidPass123!", "Email is required")
        );
    }

    @ParameterizedTest
    @MethodSource("emailValidationNegativeData")
    public void testEmailValidationNegative(String email, String password, String expectedError) {
        signInPage.inputCredentials(email, password);
        signInPage.clickSubmitButton();
        Assertions.assertEquals(expectedError, signInPage.getErrorEmailText());
    }

    private static Stream<Arguments> emailValidationNegativeData() {
        String errorMessage = "Please check that your e-mail address is indicated correctly";
        String password = "ValidPass123!";
        return Stream.of(
                Arguments.of("plainaddress", password, errorMessage),
                Arguments.of("@missingusername.com", password, errorMessage),
                Arguments.of("username@.missingdomain", password, errorMessage)
        );
    }

    @ParameterizedTest
    @MethodSource("passwordValidationNegativeData")
    public void testPasswordValidationNegative(String email, String password, String expectedError) {
        signInPage.inputCredentials(email, password);
        signInPage.clickSubmitButton();
        Assertions.assertEquals(expectedError, signInPage.getErrorPasswordText());
    }

    private static Stream<Arguments> passwordValidationNegativeData() {
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
        Assertions.assertTrue(signInPage.waitElementToDisappear(By.tagName("app-auth-modal")));
    }

    @Test
    public void testShowHidePassword() {
        signInPage.clickShowHideButtonElement();
        Assertions.assertEquals("text", signInPage.getPasswordInputAttributeType());

        signInPage.clickShowHideButtonElement();
        Assertions.assertEquals("password", signInPage.getPasswordInputAttributeType());
    }

    @Test
    public void testForgotPasswordIsClickable() {
        signInPage.clickForgotPassword();
        Assertions.assertTrue(signInPage.waitElementToBeVisible(By.tagName("app-restore-password")).isDisplayed());
    }

    @Test
    public void testSignInButtonIsClickable() {
        signInPage.clickSignUpLink();
        Assertions.assertTrue(signInPage.waitElementToBeVisible(By.tagName("app-sign-up")).isDisplayed());
    }

}
