package ysaak.anima.service;

import com.google.common.base.Preconditions;
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
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

@Service
public class StorageService {

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

        Path directoryPath = this.rootLocation
                .resolve(type.name().toLowerCase())
                .resolve(format.name().toLowerCase());

        SearchFileVisitor searchFileVisitor = new SearchFileVisitor(id);

        try {
            Files.walkFileTree(directoryPath, searchFileVisitor);

            final Path filePath = searchFileVisitor.getFoundFilePath().orElseThrow(() -> new StorageException("No file found for id " + id));

            final Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException("Could not read file: " + filePath);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("Error while searching file", e);
        }
    }

    private static class SearchFileVisitor extends SimpleFileVisitor<Path> {

        private final PathMatcher pathMatcher;
        private Path foundFilePath = null;

        private SearchFileVisitor(String id) {
            pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + id + "*");
        }

        @Override
        public FileVisitResult visitFile(Path filePath, BasicFileAttributes basicFileAttrib) {
            if (pathMatcher.matches(filePath.getFileName())) {
                foundFilePath = filePath;
                return FileVisitResult.TERMINATE;
            }

            return FileVisitResult.CONTINUE;
        }

        public Optional<Path> getFoundFilePath() {
            return Optional.ofNullable(foundFilePath);
        }

        /*
        @Override
        public FileVisitResult preVisitDirectory(Path directoryPath,
                                                 BasicFileAttributes basicFileAttrib) {
            if (pathMatcher.matches(directoryPath.getFileName())) {
                matchCount++;
                System.out.println(directoryPath);
            }
            return FileVisitResult.CONTINUE;
        }
 */
    }


}
