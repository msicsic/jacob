package sk.jacob.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Security {
    public static String md5String(String inputString) {
        String md5String;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputString.getBytes());
            byte[] dig = md.digest();
            StringBuffer sb = new StringBuffer();

            for (int i=0; i < dig.length; i++) {
                sb.append(Integer.toString( ( dig[i] & 0xff ) + 0x100, 16).substring( 1 ));
            }
            md5String = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return md5String;
    }

    private static final SecureRandom RND_GENERATOR = new SecureRandom();
    public static String uniqueToken() {
        return new BigInteger(255, RND_GENERATOR).toString(32);
    }
}
