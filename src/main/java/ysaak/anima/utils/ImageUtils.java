package ysaak.anima.utils;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public final class ImageUtils {
    private ImageUtils() { /**/ }

    public static BufferedImage resizeAndCrop(BufferedImage originalImage, int width, int height) {
        final int imageWidth = originalImage.getWidth();
        final int imageHeight = originalImage.getHeight();

        BufferedImage resizedImage;

        // Check if uploaded image is smaller than resized size
        if (imageWidth <= width && imageHeight <= height) {
            resizedImage = originalImage;
        }
        else {
            if (imageWidth < width) {
                // Smaller width than resized but taller
                resizedImage = resizeToHeight(originalImage, height);
            }
            else if (imageHeight < height) {
                // Smaller height than resized but wider
                resizedImage = resizeToWidth(originalImage, width);
            }
            else {
                // Taller and wider than resized
                if (imageWidth > imageHeight) {
                    // Landscape image : resize to height and crop

                    resizedImage = resizeToHeight(originalImage, height);
                    resizedImage = cropWidth(resizedImage, width);
                }
                else {
                    resizedImage = resizeToWidth(originalImage, width);
                    resizedImage = cropHeight(resizedImage, height);
                }
            }
        }

        return resizedImage;
    }

    private static BufferedImage resizeToHeight(BufferedImage originalImage, int height) {
        final int imageWidth = originalImage.getWidth();
        final int imageHeight = originalImage.getHeight();

        int targetWidth = (int) ((((double) imageWidth) / ((double) imageHeight)) * height);
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, targetWidth, height);
    }

    private static BufferedImage resizeToWidth(BufferedImage originalImage, int width) {
        final int imageWidth = originalImage.getWidth();
        final int imageHeight = originalImage.getHeight();

        int targetHeight = (int) ((((double) imageHeight) / ((double) imageWidth)) * width);
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, width, targetHeight);
    }

    private static BufferedImage cropWidth(BufferedImage originalImage, int width) {
        final int imageWidth = originalImage.getWidth();
        final int imageHeight = originalImage.getHeight();
        final int x = (int) (((double) imageWidth - width) / 2.0);

        return Scalr.crop(originalImage, x, 0, width, imageHeight);
    }

    private static BufferedImage cropHeight(BufferedImage originalImage, int height) {
        final int imageWidth = originalImage.getWidth();
        final int imageHeight = originalImage.getHeight();
        final int y = (int) (((double) imageHeight - height) / 2.0);

        return Scalr.crop(originalImage, 0, y, imageWidth, height);
    }
}
