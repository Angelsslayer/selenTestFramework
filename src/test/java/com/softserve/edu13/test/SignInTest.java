package com.softserve.edu13.test;

import com.softserve.edu13.data.TestUser;
import com.softserve.edu13.modules.SignInPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignInTest implements TestsRunner {
    private SignInPage signInPage;

    @BeforeEach
    public void init() {
        TestsRunner.super.init();
        signInPage = new SignInPage(driver, wait);
        signInPage.openSignInPage("/greenCity")
                .switchLanguageToEnglish();
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
    @MethodSource("com.softserve.edu13.data.TestUserRepository#validUser")
    public void testPositiveSignIn(TestUser user) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword())
                .clickSubmitButton();

        Assertions.assertTrue(signInPage.validateUserLoggedIn());
    }


    @ParameterizedTest
    @MethodSource("com.softserve.edu13.data.TestUserRepository#userWithEmptyFields")
    public void testEmptyFieldsValidation(TestUser user, String errorText) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword());

        Assertions.assertFalse(signInPage.isSubmitButtonClickable());
        Assertions.assertEquals(errorText, signInPage.getGeneralErrorText());
    }


    @ParameterizedTest
    @MethodSource("com.softserve.edu13.data.TestUserRepository#userWithEmptyPassword")
    public void testPasswordFieldEmpty(TestUser user, String errorText) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword())
                .clickSubmitButton();

        Assertions.assertEquals(errorText, signInPage.getErrorPasswordText());
    }


    @ParameterizedTest
    @MethodSource("com.softserve.edu13.data.TestUserRepository#userWithInvalidPassword")
    public void testIncorrectPasswordField(TestUser user, String errorText) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword())
                .clickSubmitButton();

        Assertions.assertEquals(errorText, signInPage.getGeneralErrorText());
    }


    @ParameterizedTest
    @MethodSource("com.softserve.edu13.data.TestUserRepository#userWithEmptyEmail")
    public void testEmailFieldEmpty(TestUser user, String errorText) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword())
                .clickSubmitButton();

        Assertions.assertEquals(errorText, signInPage.getErrorEmailText());
    }

    @ParameterizedTest
    @MethodSource("com.softserve.edu13.data.TestUserRepository#userWithIllegalEmail")
    public void testEmailValidationNegative(TestUser user, String expectedError) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword())
                .clickSubmitButton();

        Assertions.assertEquals(expectedError, signInPage.getErrorEmailText());
    }


    @ParameterizedTest
    @MethodSource("com.softserve.edu13.data.TestUserRepository#userWithIllegalPassword")
    public void testPasswordValidationNegative(TestUser user, String expectedError) {
        signInPage.inputCredentials(user.getEmail(), user.getPassword())
                .clickSubmitButton();

        Assertions.assertEquals(expectedError, signInPage.getErrorPasswordText());
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

        Assertions.assertTrue(signInPage.waitElementToBePresent(By.tagName("app-restore-password")).isDisplayed());
    }

    @Test
    public void testSignInButtonIsClickable() {
        signInPage.clickSignUpLink();

        Assertions.assertTrue(signInPage.waitElementToBePresent(By.tagName("app-sign-up")).isDisplayed());
    }

}
