package ysaak.anima.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TestImageUtils {

    @Test
    public void testSmallerInputImage_horizontal() {
        // Given
        BufferedImage sourceImage = loadImage("horizontal");
        int width = 400;
        int height = 400;

        // When
        BufferedImage image = ImageUtils.resizeAndCrop(sourceImage, width, height);

        // Then
        Assert.assertNotNull(image);
        Assert.assertEquals(width, image.getWidth());
        Assert.assertEquals(height, image.getHeight());
    }

    @Test
    public void testSmallerInputImage_vertical() {
        // Given
        BufferedImage sourceImage = loadImage("vertical");
        int width = 400;
        int height = 400;

        // When
        BufferedImage image = ImageUtils.resizeAndCrop(sourceImage, width, height);

        // Then
        Assert.assertNotNull(image);
        Assert.assertEquals(width, image.getWidth());
        Assert.assertEquals(height, image.getHeight());
    }

    @Test
    public void testLargerInputImage_horizontal() {
        // Given
        BufferedImage sourceImage = loadImage("horizontal");
        int width = 100;
        int height = 50;

        // When
        BufferedImage image = ImageUtils.resizeAndCrop(sourceImage, width, height);

        // Then
        Assert.assertNotNull(image);
        Assert.assertEquals(width, image.getWidth());
        Assert.assertEquals(height, image.getHeight());
    }

    @Test
    public void testLargerInputImage_vertical() {
        // Given
        BufferedImage sourceImage = loadImage("vertical");
        int width = 100;
        int height = 50;

        // When
        BufferedImage image = ImageUtils.resizeAndCrop(sourceImage, width, height);

        // Then
        Assert.assertNotNull(image);
        Assert.assertEquals(width, image.getWidth());
        Assert.assertEquals(height, image.getHeight());
    }


    private static BufferedImage loadImage(String orientation) {
        String path = "/images/test_" + orientation + ".png";
        InputStream is = TestImageUtils.class.getResourceAsStream(path);
        try {
            return ImageIO.read(is);
        }
        catch (IOException e) {
            throw new RuntimeException("Error while loading test image", e);
        }
    }
}
