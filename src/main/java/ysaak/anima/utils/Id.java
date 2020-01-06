package ysaak.anima.utils;

import java.security.SecureRandom;
import java.util.Base64;

public final class Id {
    private Id() { /**/ }

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generate() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }
}
