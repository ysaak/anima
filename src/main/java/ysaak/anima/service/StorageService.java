package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ysaak.anima.config.StorageConfig;
import ysaak.anima.data.storage.StorageFormat;
import ysaak.anima.data.storage.StorageType;
import ysaak.anima.exception.StorageException;
import ysaak.anima.utils.ImageUtils;

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

    public void store(final StorageType type, final String id, final MultipartFile file) throws StorageException {
        Preconditions.checkNotNull(type, "type is null");
        Preconditions.checkNotNull(id, "id is null");
        Preconditions.checkNotNull(file, "file is null");

        if (ysaak.anima.utils.StringUtils.isNotEmpty(file.getOriginalFilename())) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (filename.contains("..")) {
                    // This is a security check
                    throw new StorageException("Cannot store file with relative path outside current directory " + filename);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    final BufferedImage originalImage = ImageIO.read(inputStream);

                    // Store full image
                    final BufferedImage fullImage = storeImage(originalImage, type, StorageFormat.FULL, id);
                    storeImage(fullImage, type, StorageFormat.THUMBNAIL, id);
                }
            }
            catch (IOException e) {
                throw new StorageException("Failed to store file " + filename, e);
            }
        }
        else {
            throw new StorageException("Failed to store empty file");
        }
    }

    private BufferedImage storeImage(BufferedImage image, StorageType type, StorageFormat format, String id) throws StorageException {
        StorageConfig.Size fullSize = storageConfig.get(type, format)
                .orElseThrow(() -> new StorageException("No size defined for couple " + type + "/" + format));

        final BufferedImage resizedImage = ImageUtils.resizeAndCrop(image, fullSize.getWidth(), fullSize.getHeight());
        Path destinationPath = getLocalFileLocation(type, format, id);

        try {

            if (!Files.exists(destinationPath.getParent())) {
                Files.createDirectories(destinationPath.getParent());
            }

            ImageIO.write(resizedImage, "png", destinationPath.toFile());
        }
        catch (IOException e) {
            throw new StorageException("Error while writing image " + destinationPath.toString(), e);
        }

        return resizedImage;
    }

    private Path getLocalFileLocation(final StorageType type, final StorageFormat format, final String id) {
        return this.rootLocation
                .resolve(type.name().toLowerCase())
                .resolve(format.name().toLowerCase())
                .resolve(id + ".png");
    }

    public Resource getImage(final StorageType type, final StorageFormat format, String id) throws StorageException {
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

    private Resource getNoImageResource(final StorageType type, final StorageFormat format) throws StorageException {
        final StorageConfig.Size configSize = storageConfig.get(type, format)
                .orElseThrow(() -> new StorageException("No size defined for couple " + type + "/" + format));

        final String imageKey = "public/images/no_image/" + configSize.getWidth() + "x" + configSize.getHeight() + ".png";
        return new ClassPathResource(imageKey);
    }
}
