package ysaak.anima.utils;

import ysaak.anima.exception.ErrorCode;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.GenericErrorCode;

import java.net.MalformedURLException;
import java.net.URL;

public class Validate {
    public static void notNull(Object object, String name) throws FunctionalException {
        notNull(object, GenericErrorCode.NULL_VALUE, name);
    }

    public static void notNull(Object object, ErrorCode error, Object...args) throws FunctionalException {
        if (object == null) {
            throw error.functional(args);
        }
    }

    /**
     * Check if the input string is not blank
     * @param string Input string
     * @param errorCode Error code
     * @param args Error code arguments
     * @throws FunctionalException Throw if the input string is blank
     */
    public static void notBlank(String string, ErrorCode errorCode, Object...args) throws FunctionalException {
        if (StringUtils.isBlank(string)) {
            throw errorCode.functional(args);
        }
    }

    /**
     * Check is the input string has a valid length (between [min, max])
     * @param string Input string
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @param errorCode Error code
     * @param args Error code arguments
     * @throws FunctionalException Thrown if the input string has an invalid length
     */
    public static void length(String string, int minLength, int maxLength, ErrorCode errorCode, Object...args) throws FunctionalException {
        if (string == null || string.length() < minLength || maxLength < string.length()) {
            throw  errorCode.functional(args);
        }
    }

    /**
     * Check is the input string has a valid length (between [min, inf.])
     * @param string Input string
     * @param minLength Minimum length
     * @param errorCode Error code
     * @param args Error code arguments
     * @throws FunctionalException Thrown if the input string has an invalid length
     */
    public static void minLength(String string, int minLength, ErrorCode errorCode, Object...args) throws FunctionalException {
        length(string, minLength, Integer.MAX_VALUE, errorCode, args);
    }

    /**
     * Check is the input string has a valid length (between [0, max])
     * @param string Input string
     * @param maxLength Maximum length
     * @param errorCode Error code
     * @param args Error code arguments
     * @throws FunctionalException Thrown if the input string has an invalid length
     */
    public static void maxLength(String string, int maxLength, ErrorCode errorCode, Object...args) throws FunctionalException {
        length(string, 0, maxLength, errorCode, args);
    }

    /**
     * Check if the input string is a a valid URL
     * @param url Input string
     * @param errorCode Error code
     * @param args Error code arguments
     * @throws FunctionalException Thrown if the input string is an invalid URL
     */
    public static void validUrl(String url, ErrorCode errorCode, Object...args) throws FunctionalException {
        try {
            new URL(url);
        }
        catch (MalformedURLException e) {
            throw errorCode.functional(e, args);
        }
    }

    /**
     * Check if the expression is true
     * @param expression Expression to test
     * @param errorCode Error code
     * @param args Error code argument
     * @throws FunctionalException Thrown if the expression is false
     */
    public static void isTrue(boolean expression, ErrorCode errorCode, Object...args) throws FunctionalException {
        if (!expression) {
            throw errorCode.functional(args);
        }
    }

    /**
     * Check if the expression is false
     * @param expression Expression to test
     * @param errorCode Error code
     * @param args Error code argument
     * @throws FunctionalException Thrown if the expression is true
     */
    public static void isFalse(boolean expression, ErrorCode errorCode, Object...args) throws FunctionalException {
        if (expression) {
            throw errorCode.functional(args);
        }
    }
}
