package ysaak.anima.utils;

public class StringUtils {
    public static boolean isNotEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public static String getNotNull(String string) {
        return isNotEmpty(string) ? string : "";
    }

    public static int extractInt(String str) {
        String num = str.replaceAll("\\D", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
}
