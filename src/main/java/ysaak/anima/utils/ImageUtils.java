package ysaak.anima.utils;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public final class ImageUtils {
    private ImageUtils() { /**/ }

    public static BufferedImage resizeAndCrop(BufferedImage originalImage, int targetWidth, int targetHeight) {
        final int imageWidth = originalImage.getWidth();
        final int imageHeight = originalImage.getHeight();

        float widthRatio = (float) imageWidth / targetWidth;
        float heightRatio =(float) imageHeight / targetHeight;

        int resizedWidth;
        int resizedHeight;

        boolean cropWidth = false;
        boolean cropHeight = false;

        if(widthRatio > heightRatio) { //shrink to fixed height
            resizedWidth = Math.round(originalImage.getWidth() / heightRatio);
            resizedHeight = targetHeight;
            cropWidth = resizedWidth > targetWidth;
        }
        else { //shrink to fixed width
            resizedWidth = targetWidth;
            resizedHeight = Math.round(originalImage.getHeight() / widthRatio);
            cropHeight = resizedHeight > targetHeight;
        }

        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, resizedWidth, resizedHeight);

        if (cropHeight) {
            resizedImage = cropHeight(resizedImage, targetHeight);
        }
        if (cropWidth) {
            resizedImage = cropWidth(resizedImage, targetWidth);
        }

        return resizedImage;
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
