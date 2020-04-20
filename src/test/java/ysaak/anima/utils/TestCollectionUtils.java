package ysaak.anima.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestCollectionUtils {

    @Test
    public void testIsCollectionOfType_emptyList() {
        // Given
        final List<Object> emptyList = Collections.emptyList();
        final boolean expectedResult = false;

        // When
        final boolean result = CollectionUtils.isCollectionOfType(emptyList, String.class);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsCollectionOfType_validType() {
        // Given
        final List<String> list = new ArrayList<>();
        list.add("String2");

        final boolean expectedResult = true;

        // When
        final boolean result = CollectionUtils.isCollectionOfType(list, String.class);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsCollectionOfType_validSubType() {
        // Given
        final List<Integer> list = new ArrayList<>();
        list.add(1);

        final boolean expectedResult = true;

        // When
        final boolean result = CollectionUtils.isCollectionOfType(list, Number.class);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsCollectionOfType_invalidType() {
        // Given
        final List<String> list = new ArrayList<>();
        list.add("String2");

        final boolean expectedResult = false;

        // When
        final boolean result = CollectionUtils.isCollectionOfType(list, Integer.class);

        // Then
        Assert.assertEquals(expectedResult, result);
    }
}
