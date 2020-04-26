package ysaak.anima.utils;

public class StringUtils {
    public static final String EMPTY = "";

    /**
     * Check if the input string is blank : null or empty trimmed
     * @param string input string
     * @return true if the input string is blank - false otherwise
     */
    public static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Check if the input string is not blank : not null and not empty trimmed
     * @param string input string
     * @return true if the input string is not blank - false otherwise
     */
    public static boolean isNotBlank(String string) {
        return string != null && !string.trim().isEmpty();
    }

    /**
     * Check if the input string is not blank. If it is blank, an empty string ("") is returned
     * @param string input string
     * @return the input string if not blank - an empty one otherwise
     */
    public static String getNotNull(String string) {
        return isNotBlank(string) ? string : EMPTY;
    }

    /**
     * Extract the digits in the input string
     * @param str Input string
     * @return the digits in the string - 0 if not digits found
     */
    public static int extractDigits(String str) {
        String num = str.replaceAll("\\D", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
}
