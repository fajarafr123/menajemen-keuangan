package com.example.apkmenajemenkeuangan.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtil {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Menggunakan algoritma SHA-256
            byte[] hashedBytes = digest.digest(password.getBytes()); // Enkripsi string ke byte array
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // Mengubah byte ke format hex
            }
            return sb.toString(); // Return hasil hashing
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Jika gagal hashing, return null
        }
    }
}
