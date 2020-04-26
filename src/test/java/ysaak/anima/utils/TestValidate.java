package ysaak.anima.utils;

import org.junit.Assert;
import org.junit.Test;
import ysaak.anima.exception.ErrorCode;
import ysaak.anima.exception.FunctionalException;

public class TestValidate {
    @Test
    public void testNotNull_nullValue_genericException() {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        try {
            Validate.notNull(null, errorCode, "arg");

            Assert.fail("Exception should have been thrown");
        }
        catch (FunctionalException e) {
            // Check if thrown exception is the correct one
            Assert.assertEquals(errorCode, e.getError());
        }
    }

    @Test
    public void testNotNull_nullValue_customException() {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        try {
            Validate.notNull(null, errorCode, "arg");

            Assert.fail("Exception should have been thrown");
        }
        catch (FunctionalException e) {
            // Check if thrown exception is the correct one
            Assert.assertEquals(errorCode, e.getError());
        }
    }

    @Test
    public void testNotNull_notNullValue() throws FunctionalException {
        // Given
        Object testObject = new Object();
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.notNull(testObject, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testNotBlank_blankValue() throws FunctionalException {
        // Given
        String testValue = " ";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.notBlank(testValue, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testNotBlank_notBlankValue() throws FunctionalException {
        // Given
        String testValue = " text ";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.notBlank(testValue, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testLength_nullValue() throws FunctionalException {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.length(null, 5, 10, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test(expected = FunctionalException.class)
    public void testLength_lessThanMinLength() throws FunctionalException {
        // Given
        String testValue = "12";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.length(testValue, 5, 10, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test(expected = FunctionalException.class)
    public void testLength_moreThanMaxLength() throws FunctionalException {
        // Given
        String testValue = "123456789012";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.length(testValue, 5, 10, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testLength_minLengthValue() throws FunctionalException {
        // Given
        String testValue = "12345";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.length(testValue, 5, 10, errorCode, "arg");

        // Then
        // Success
    }

    @Test
    public void testLength_maxLengthValue() throws FunctionalException {
        // Given
        String testValue = "1234567890";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.length(testValue, 5, 10, errorCode, "arg");

        // Then
        // Success
    }

    @Test
    public void testLength_betweenLengthValue() throws FunctionalException {
        // Given
        String testValue = "1234567";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.length(testValue, 5, 10, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testMinLength_lessThanMinLength() throws FunctionalException {
        // Given
        String testValue = "123";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.minLength(testValue, 5, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testMinLength_MoreThanMinLength() throws FunctionalException {
        // Given
        String testValue = "123456";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.minLength(testValue, 5, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testMinLength_MoreThanMaxLength() throws FunctionalException {
        // Given
        String testValue = "123456";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.maxLength(testValue, 5, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testMaxLength_LessThanMaxLength() throws FunctionalException {
        // Given
        String testValue = "123";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.maxLength(testValue, 5, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testValidUrl_invalid() throws FunctionalException {
        // Given
        String testValue = "blob";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.validUrl(testValue, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testValidUrl_valid() throws FunctionalException {
        // Given
        String testValue = "https://www.qwant.com/?q=%s";
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.validUrl(testValue, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testIsTrue_invalid() throws FunctionalException {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.isTrue(false, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testIsTrue_valid() throws FunctionalException {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.isTrue(true, errorCode, "arg");

        // Then
        // Success
    }

    @Test(expected = FunctionalException.class)
    public void testIsFalse_invalid() throws FunctionalException {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.isFalse(true, errorCode, "arg");

        // Then
        Assert.fail("Exception expected");
    }

    @Test
    public void testIsFalse_valid() throws FunctionalException {
        // Given
        ErrorCode errorCode = TestErrorCode.TEST;

        // When
        Validate.isFalse(false, errorCode, "arg");

        // Then
        // Success
    }


    private enum TestErrorCode implements ErrorCode {
        TEST("CODE", "MESSAGE %s")
        ;

        private final String code;
        private final String message;

        TestErrorCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
