package kz.codingwolves.identicons;

import java.security.MessageDigest;

public class HashGenerator {

    private MessageDigest messageDigest;

    public HashGenerator(String algorithim) {
        try {
            messageDigest = MessageDigest.getInstance(algorithim);
        } catch(Exception e) {}
    }

    public byte[] generate(String input) {
        return messageDigest.digest(input.getBytes());
    }
}