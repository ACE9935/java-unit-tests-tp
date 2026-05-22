package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void testValidPassword_Classic() {
        assertTrue(validator.isValid("Password1!"));
        assertEquals("Password is valid", validator.getErrorMessage("Password1!"));
    }

    @Test
    void testNullPassword_Classic() {
        assertFalse(validator.isValid(null));
        assertEquals("Password must not be null", validator.getErrorMessage(null));
    }

    @ParameterizedTest
    @CsvSource({
            "Password1!, true,  Password is valid",
            "Admin2024@, true,  Password is valid",
            "short1!,    false, Password must contain at least 8 characters",
            "PASSWORD1!, false, Password must contain at least one lowercase letter",
            "password1!, false, Password must contain at least one uppercase letter",
            "Password!,  false, Password must contain at least one digit",
            "Password1,  false, Password must contain at least one special character"
    })
    void testPasswordRules_CsvSource(String password, boolean expectedValid, String expectedMessage) {
        assertEquals(expectedValid, validator.isValid(password));
        assertEquals(expectedMessage, validator.getErrorMessage(password));
    }

    // ==========================================
    // 3. TEST PARAMÉTRÉ AVEC @ValueSource
    // ==========================================
    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@", "ValidP@ss123", "Super#2026"})
    void testValidPasswords_ValueSource(String password) {
        assertTrue(validator.isValid(password));
        assertEquals("Password is valid", validator.getErrorMessage(password));
    }
    @ParameterizedTest
    @MethodSource("provideInvalidPasswordsAndMessages")
    void testInvalidPasswords_MethodSource(String password, String expectedMessage) {
        assertFalse(validator.isValid(password));
        assertEquals(expectedMessage, validator.getErrorMessage(password));
    }

    private static Stream<Arguments> provideInvalidPasswordsAndMessages() {
        return Stream.of(
                Arguments.of("abc", "Password must contain at least 8 characters"),
                Arguments.of("AAA111!!!", "Password must contain at least one lowercase letter"),
                Arguments.of("aaa111!!!", "Password must contain at least one uppercase letter"),
                Arguments.of("Aaaaaaaa!!!", "Password must contain at least one digit"),
                Arguments.of("Aaaaaaaa111", "Password must contain at least one special character")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullAndEmptyPasswords_Bonus(String password) {
        assertFalse(validator.isValid(password));
        
        if (password == null) {
            assertEquals("Password must not be null", validator.getErrorMessage(password));
        } else {
            // Une chaîne vide "" fait moins de 8 caractères, elle déclenche donc cette règle en premier
            assertEquals("Password must contain at least 8 characters", validator.getErrorMessage(password));
        }
    }
}