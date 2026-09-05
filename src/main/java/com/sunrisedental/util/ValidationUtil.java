package com.sunrisedental.util;

import java.time.LocalDate;

public class ValidationUtil {
    public static boolean isValidEmail(String email) {
        return email!=null&&email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    public static void required(String value,String field) {
        if(value==null||value.trim().isEmpty()) {
            throw new IllegalArgumentException(field+" is required.");
        }
    }
    public static void appointmentDate(String value) {
        required(value,"Appointment date");
        if(LocalDate.parse(value).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }
    }
}
