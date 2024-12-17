package com.spsa.strategy.config;
import java.util.Base64;

public class Base64Encoder {
    public static String encode(String data) {
        byte[] encodedBytes = Base64.getEncoder().encode(data.getBytes());
        return new String(encodedBytes);
    }

    public static String decode(String encodedData) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
        return new String(decodedBytes);
    }
}