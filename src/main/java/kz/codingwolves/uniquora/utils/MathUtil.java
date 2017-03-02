package kz.codingwolves.uniquora.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created by Yegor on 02/03/17
 */
public class MathUtil {

    public static Integer totalPages(Integer totalCount, Integer pageSize) {
        return new Double(Math.ceil(totalCount.doubleValue() / pageSize)).intValue();
    }

    public static String shaHash(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StringBuffer hexString = new StringBuffer();
            byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return string;
        }
    }
}
