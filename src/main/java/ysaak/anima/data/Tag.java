package ysaak.anima.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ysaak.anima.service.validation.ValidationMessages;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Tag {
    private String id;

    @NotEmpty
    @Size(max = 250, message = ValidationMessages.MAX_LENGTH)
    private String name;

    @Size(max = 4000, message = ValidationMessages.MAX_LENGTH)
    private String description;

    private Multimap<TagEquivalenceOrigin, String> equivalenceMap;

    public Tag() {
    }

    public Tag(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.equivalenceMap = HashMultimap.create();
    }

    public Tag(String id, String name, String description, Multimap<TagEquivalenceOrigin, String> equivalenceMap) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.equivalenceMap = equivalenceMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Multimap<TagEquivalenceOrigin, String> getEquivalenceMap() {
        return equivalenceMap;
    }

    public void setEquivalenceMap(Multimap<TagEquivalenceOrigin, String> equivalenceMap) {
        this.equivalenceMap = equivalenceMap;
    }

    public enum TagEquivalenceOrigin {
        ANIDB
    }
}
