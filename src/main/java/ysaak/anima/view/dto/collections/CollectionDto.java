package ysaak.anima.view.dto.collections;

import ysaak.anima.data.ElementType;

import java.util.Map;

public class CollectionDto {
    private final String id;
    private final String name;

    private final Map<ElementType, Integer> countByType;
    private final int elementCount;

    public CollectionDto(String id, String name, Map<ElementType, Integer> countByType, int elementCount) {
        this.id = id;
        this.name = name;
        this.countByType = countByType;
        this.elementCount = elementCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<ElementType, Integer> getCountByType() {
        return countByType;
    }

    public int getElementCount() {
        return elementCount;
    }
}
