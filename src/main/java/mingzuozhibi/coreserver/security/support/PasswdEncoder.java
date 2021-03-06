package mingzuozhibi.coreserver.security.support;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

public abstract class PasswdEncoder {

    private static final String SALT1 = "mingzuozhibi.com/1";
    private static final String SALT2 = "mingzuozhibi.com/2";
    private static final String SALT3 = "mingzuozhibi.com/3";

    public static String encode(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        return md5(md5(username + SALT1) + md5(password + SALT2) + SALT3);
    }

    private static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            ret.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[b & 0x0f]);
        }
        return ret.toString();
    }

}
