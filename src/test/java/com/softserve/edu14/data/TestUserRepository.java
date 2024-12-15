package com.softserve.edu14.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestUserRepository {
    public static Stream<Arguments> validUser() {
        return Stream.of(
                Arguments.of(new TestUser("validuser@greencity.com", "ValidPass123!"))
        );
    }

    private static Stream<Arguments> userWithEmptyFields() {
        return Stream.of(
                Arguments.of(new TestUser("", ""), "Please fill all red fields")
        );
    }

    public static Stream<Arguments> userWithEmptyPassword() {
        return Stream.of(
                Arguments.of(new TestUser("invaliduser@greencity.com", ""), "Password is required")
        );
    }

    private static Stream<Arguments> userWithInvalidPassword() {
        return Stream.of(
                Arguments.of(new TestUser("validuser@greencity.com", "IncorrectPass1!"), "Bad email or password")
        );
    }

    private static Stream<Arguments> userWithEmptyEmail() {
        return Stream.of(
                Arguments.of(new TestUser("", "ValidPass123!"), "Email is required")
        );
    }


    private static Stream<Arguments> userWithIllegalEmail() {
        String errorMessage = "Please check that your e-mail address is indicated correctly";
        String password = "ValidPass123!";

        return Stream.of(
                Arguments.of(new TestUser("plainaddress", password), errorMessage),
                Arguments.of(new TestUser("@missingusername.com", password), errorMessage),
                Arguments.of(new TestUser("username@.missingdomain", password), errorMessage)
        );
    }


    private static Stream<Arguments> userWithIllegalPassword() {
        String email = "validuser@greencity.com";
        String generalErrorMessage = "Password have from 8 to 20 characters long without spaces and contain " +
                "at least one uppercase letter (A-Z), one lowercase letter (a-z), a digit (0-9), " +
                "and a special character (~`!@#$%^&*()+=_-{}[]|:;”’?/<>,.)";

        return Stream.of(
                Arguments.of(new TestUser(email, "7Chars!"),
                        "Password must be at least 8 characters long without spaces"),
                Arguments.of(new TestUser(email, "with whiteSpace1!"), generalErrorMessage),
                Arguments.of(new TestUser(email, "21CharactersLongPass!"),
                        "Password must be less than 20 characters long without spaces."),
                Arguments.of(new TestUser(email, "NoSpecialChar1"), generalErrorMessage),
                Arguments.of(new TestUser(email, "NoDigitChar!"), generalErrorMessage),
                Arguments.of(new TestUser(email, "no_upper_case_char1"), generalErrorMessage)
        );
    }

}