package utils;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtils {

    public static String hashPassword(String plain) {
        if (plain == null || plain.isEmpty()) return plain;
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plain, String hashed) {
        if (plain == null || hashed == null) return false;
        try {
            return BCrypt.checkpw(plain, hashed);
        } catch (Exception e) {
            return plain.equals(hashed);
        }
    }

    public static boolean isBcrypt(String password) {
        return password != null
                && password.startsWith("$2a$")
                && password.length() == 60;
    }

    public static String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public static String h(String input) {
        return escapeHtml(input);
    }
}
