package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ysaak.anima.config.StorageConfig;
import ysaak.anima.data.storage.StorageFormat;
import ysaak.anima.data.storage.StorageType;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.StorageErrorCode;
import ysaak.anima.utils.ImageUtils;
import ysaak.anima.utils.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private final StorageConfig storageConfig;

    private final Path rootLocation;

    public StorageService(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
        rootLocation = Paths.get("storage");
    }

    public void store(final StorageType type, final String id, final MultipartFile file) throws FunctionalException {
        Preconditions.checkNotNull(type, "type is null");
        Preconditions.checkNotNull(id, "id is null");
        Preconditions.checkNotNull(file, "file is null");

        if (StringUtils.isNotBlank(file.getOriginalFilename())) {
            String filename = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());

            if (filename.contains("..")) {
                // This is a security check
                throw StorageErrorCode.FILE_OUTSIDE_DIRECTORY.functional(filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                final BufferedImage originalImage = ImageIO.read(inputStream);

                // Store full image
                final BufferedImage fullImage = storeImage(originalImage, type, StorageFormat.FULL, id);
                storeImage(fullImage, type, StorageFormat.THUMBNAIL, id);
            }
            catch (IOException e) {
                throw StorageErrorCode.IMAGE_READ_ERROR.functional(e, filename);
            }
        }
        else {
            throw StorageErrorCode.EMPTY_FILE_NAME.functional();
        }
    }

    private BufferedImage storeImage(BufferedImage image, StorageType type, StorageFormat format, String id) throws FunctionalException {
        StorageConfig.Size fullSize = storageConfig.get(type, format)
                .orElseThrow(() -> StorageErrorCode.MISSING_STORAGE_CONFIGURATION.functional(type, format));

        final BufferedImage resizedImage = ImageUtils.resizeAndCrop(image, fullSize.getWidth(), fullSize.getHeight());
        Path destinationPath = getLocalFileLocation(type, format, id);

        try {
            if (!Files.exists(destinationPath.getParent())) {
                Files.createDirectories(destinationPath.getParent());
            }

            ImageIO.write(resizedImage, "png", destinationPath.toFile());
        }
        catch (IOException e) {
            throw StorageErrorCode.IMAGE_WRITE_ERROR.functional(e, destinationPath.toString());
        }

        return resizedImage;
    }

    private Path getLocalFileLocation(final StorageType type, final StorageFormat format, final String id) {
        return this.rootLocation
                .resolve(type.name().toLowerCase())
                .resolve(format.name().toLowerCase())
                .resolve(id + ".png");
    }

    public Resource getImage(final StorageType type, final StorageFormat format, String id) throws FunctionalException {
        final Path imagePath = getLocalFileLocation(type, format, id);

        boolean loadDefaultImage = true;
        Resource resource = null;

        try {
            resource = new UrlResource(imagePath.toUri());

            if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
                loadDefaultImage = false;
            }
            else {
                LOGGER.error("Cannot access image " + imagePath.toString());
            }
        }
        catch (IOException e) {
            LOGGER.error("Error while loading image " + imagePath.toString(), e);
        }

        if (loadDefaultImage) {
            resource = getNoImageResource(type, format);
        }

        return resource;
    }

    private Resource getNoImageResource(final StorageType type, final StorageFormat format) throws FunctionalException {
        final StorageConfig.Size configSize = storageConfig.get(type, format)
                .orElseThrow(() -> StorageErrorCode.MISSING_STORAGE_CONFIGURATION.functional(type, format));

        final String imageKey = "public/images/no_image/" + configSize.getWidth() + "x" + configSize.getHeight() + ".png";
        return new ClassPathResource(imageKey);
    }
}
