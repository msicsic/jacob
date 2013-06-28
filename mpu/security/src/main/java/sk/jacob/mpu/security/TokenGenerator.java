package sk.jacob.mpu.security;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TokenGenerator {
    private static final SecureRandom RND_GENERATOR = new SecureRandom();
    public static String getToken() {
        return new BigInteger(255, RND_GENERATOR).toString(32);
    }
}
