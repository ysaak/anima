package ysaak.anima.config;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Component;
import ysaak.anima.data.storage.StorageFormat;
import ysaak.anima.data.storage.StorageType;

import java.util.Optional;

@Component
public class StorageConfig {
    private final Table<StorageType, StorageFormat, Size> storageSizeTable;

    public StorageConfig() {
        storageSizeTable = ImmutableTable.<StorageType, StorageFormat, Size>builder()
                .put(StorageType.ELEMENT, StorageFormat.FULL, new Size(300, 390))
                .put(StorageType.ELEMENT, StorageFormat.THUMBNAIL, new Size(70, 70))
                .build();
    }

    public Optional<Size> get(StorageType type, StorageFormat format) {
        return Optional.ofNullable(storageSizeTable.get(type, format));
    }

    public static class Size {
        private final int width;
        private final int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
