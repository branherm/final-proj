package edu.guilford;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A utility class for password encryption using SHA-1 hashing with salt.
 */
public class passwordEncryption {
    /**
     * Generates a SHA-1 hash of the password with the provided salt.
     * @param passwordToHash the password to encrypt
     * @param salt the salt value to use for hashing
     * @return the hexadecimal string representation of the hashed password
     */
    public static String get_SHA_1_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder str = new StringBuilder();
            for (byte aByte : bytes) {
                str.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = str.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * Generates a random salt value for password hashing.
     * @return a randomly generated salt
     * @throws NoSuchAlgorithmException if the secure random algorithm is not available
     */
    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}