package kz.codingwolves.uniquora.utils;

import java.security.MessageDigest;

/**
 * Created by sagynysh on 1/7/17.
 */
public class MathUtil {

    public static Integer totalPages(Integer totalCount, Integer pageSize) {
        return new Double(Math.ceil(totalCount.doubleValue() / pageSize)).intValue();
    }

    public static String md5(String string) {
        try {
            StringBuffer hexString = new StringBuffer();
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes());
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            return hexString.toString();
        } catch (Exception e) {
            return string;
        }
    }
}
