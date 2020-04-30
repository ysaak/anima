package ysaak.anima.view.dto.admin.importer;

import java.util.List;

public class TagEquivalenceDto {
    private final String tagId;
    private final String tagName;
    private final List<String> equivalenceList;

    public TagEquivalenceDto(String tagId, String tagName, List<String> equivalenceList) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.equivalenceList = equivalenceList;
    }

    public String getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public List<String> getEquivalenceList() {
        return equivalenceList;
    }
}
