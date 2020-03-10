package ysaak.anima.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Optional;

@Service
public class CacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

    private final Path rootLocation;

    public CacheService() {
        rootLocation = Paths.get("cache");
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> Optional<T> get(Cache cache, String key) {
        Path cacheFile = getCacheFileLocation(cache, key);

        T data = null;

        if (Files.exists(cacheFile)) {

            boolean deleteCacheFile = isCacheExpired(cache, cacheFile);

            if (deleteCacheFile) {
                try {
                    Files.deleteIfExists(cacheFile);
                }
                catch (IOException e) {
                    LOGGER.error("Error while deleting file '" + cacheFile.toString() + "'", e);
                }
            }

            try (FileInputStream fis = new FileInputStream(cacheFile.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                data = (T) ois.readObject();
            }
            catch(IOException|ClassNotFoundException e) {
                LOGGER.error("Error while reading cache file '" + cacheFile.toString() + "'", e);
            }
        }

        return Optional.ofNullable(data);
    }

    private Path getCacheFileLocation(final Cache type, final String key) {
        return this.rootLocation
                .resolve(type.name().toLowerCase())
                .resolve(key + ".dat");
    }

    private boolean isCacheExpired(Cache type, Path cacheFile) {
        boolean fileExpired = false;

        FileTime createTime = null;
        try {
            createTime = Files.getLastModifiedTime(cacheFile);
        }
        catch (IOException e) {
            LOGGER.error("Error while fetching last modified time of file '" + cacheFile.toString() + "'", e);
            fileExpired = true;
        }

        if (createTime != null) {
            long fileTimestamp = createTime.toMillis();
            long thresholdTime = System.currentTimeMillis() - type.getTtl();

            fileExpired = fileTimestamp < thresholdTime;
        }

        return fileExpired;
    }

    public <T extends Serializable> void store(final Cache type, final String key, T data) {
        final Path cacheFile = getCacheFileLocation(type, key);

        boolean writeData = true;

        if (Files.notExists(cacheFile.getParent())) {
            try {
                Files.createDirectories(cacheFile.getParent());
            }
            catch (IOException e) {
                LOGGER.error("Error while creating cache directory", e);
                writeData = false;
            }
        }

        if (writeData) {
            try (FileOutputStream fos = new FileOutputStream(cacheFile.toFile());
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(data);
            } catch (IOException e) {
                LOGGER.error("Error while writing file '" + cacheFile.toString() + "'", e);
            }
        }
    }
}
