package com.sunrisedental.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {
    @Test void validEmail() {
        assertTrue(ValidationUtil.isValidEmail("patient@example.com"));
    }
    @Test void invalidEmail() {
        assertFalse(ValidationUtil.isValidEmail("patient.example.com"));
    }
    @Test void emptyName() {
        assertThrows(IllegalArgumentException.class,()->ValidationUtil.required("","Patient name"));
    }
    @Test void pastDate() {
        assertThrows(IllegalArgumentException.class,()->ValidationUtil.appointmentDate("2020-01-01"));
    }
}
