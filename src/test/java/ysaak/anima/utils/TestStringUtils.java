package ysaak.anima.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestStringUtils {
    @Test
    public void testIsBlank_blankValues() {
        // Given
        List<String> blankStringList = Arrays.asList(
                null,
                "",
                "     ",
                "\t",
                "\t     ",
                "\n"
        );

        // When
        for (int i = 0; i < blankStringList.size(); i++) {

            String stringToTest = blankStringList.get(i);
            if (!StringUtils.isBlank(stringToTest)) {
                Assert.fail(String.format("String '%s' (index %d) is not considered as blank", stringToTest, i));
            }
        }
    }

    @Test
    public void testIsBlank_notBlankValues() {
        // Given
        List<String> notBlankStringList = Arrays.asList(
                "text",
                " text "
        );

        // When
        for (int i = 0; i < notBlankStringList.size(); i++) {

            String stringToTest = notBlankStringList.get(i);
            if (StringUtils.isBlank(stringToTest)) {
                Assert.fail(String.format("String '%s' (index %d) is considered as blank", stringToTest, i));
            }
        }
    }
    @Test
    public void testIsNotBlank_blankValues() {
        // Given
        List<String> blankStringList = Arrays.asList(
                null,
                "",
                "     ",
                "\t",
                "\t     ",
                "\n"
        );

        // When
        for (int i = 0; i < blankStringList.size(); i++) {

            String stringToTest = blankStringList.get(i);
            if (StringUtils.isNotBlank(stringToTest)) {
                Assert.fail(String.format("String '%s' (index %d) is not considered as blank", stringToTest, i));
            }
        }
    }

    @Test
    public void testIsNotBlank_notBlankValues() {
        // Given
        List<String> notBlankStringList = Arrays.asList(
                "text",
                " text "
        );

        // When
        for (int i = 0; i < notBlankStringList.size(); i++) {

            String stringToTest = notBlankStringList.get(i);
            if (!StringUtils.isNotBlank(stringToTest)) {
                Assert.fail(String.format("String '%s' (index %d) is considered as blank", stringToTest, i));
            }
        }
    }

    @Test
    public void testGetNotNull_nullValue() {
        // Given
        String nullString = null;
        String expectedResult = StringUtils.EMPTY;

        // When
        String result = StringUtils.getNotNull(nullString);

        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNotNull_notNullValue() {
        // Given
        String notNullString = "text";

        // When
        String result = StringUtils.getNotNull(notNullString);

        // Then
        Assert.assertNotNull(result);
        Assert.assertSame(notNullString, result);
    }

    @Test
    public void testExtractDigits_withoutDigits() {
        // Given
        String testString = "no_digit";
        int expectedResult = 0;

        // When
        int result = StringUtils.extractDigits(testString);

        // Assert
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testExtractDigits_withDigits() {
        // Given
        String testString = "S123LKJ456H";
        int expectedResult = 123456;

        // When
        int result = StringUtils.extractDigits(testString);

        // Assert
        Assert.assertEquals(expectedResult, result);
    }
}
