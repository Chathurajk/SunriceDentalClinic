package com.sunrisedental.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtil {
    public static String sha256(String value) {
        try {
            byte[] bytes=MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result=new StringBuilder();
            for(byte b:bytes) {
                result.append(String.format("%02x",b));
            }
            return result.toString();
        }
        catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
