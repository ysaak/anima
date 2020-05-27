package ysaak.anima.view.dto;

import ysaak.anima.data.ElementType;

import java.util.Map;

public class TagDto {
    private final String id;
    private final String name;
    private final String description;
    private final Map<ElementType, Integer> countByType;
    private final int elementCount;

    public TagDto(String id, String name, String description, Map<ElementType, Integer> countByType, int elementCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.countByType = countByType;
        this.elementCount = elementCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<ElementType, Integer> getCountByType() {
        return countByType;
    }

    public int getElementCount() {
        return elementCount;
    }
}
