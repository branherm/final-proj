package edu.guilford;

import java.security.SecureRandom;

/**
 * A utility class for generating secure random passwords.
 */
public class PasswordGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String small_caps = "abcdefghijklmnopqrstuvwxyz";
    private static final String Numeric = "1234567890";
    private static final String special_char = "~!@#$%^&*(_+{}|:_[?]>=<";
    private static final String dic = caps + small_caps + Numeric + special_char;

    /**
     * Generates a secure random password of the specified length.
     * @param len the desired length of the password (minimum 8 recommended)
     * @return the generated password
     */
    public String generatePassword(int len) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            password.append(dic.charAt(index));
        }
        return password.toString();
    }
}
