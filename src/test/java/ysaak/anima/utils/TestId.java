package ysaak.anima.utils;

import org.junit.Assert;
import org.junit.Test;

public class TestId {

    @Test
    public void testGenerate() {
        // Given
        int maxLength = 30;

        // When
        String id = Id.generate();

        // Then
        Assert.assertNotNull(id);
        Assert.assertTrue(id.length() <= maxLength);
    }
}
