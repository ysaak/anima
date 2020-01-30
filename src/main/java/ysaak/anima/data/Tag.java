package ysaak.anima.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Tag implements Entity {
    private String id;

    @NotEmpty(message = "{validation.name.notEmpty}")
    @Size(max = 250, message = "{validation.name.size}")
    private String name;

    @Size(max = 4000, message = "{validation.description.size}")
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

    @Override
    public String getId() {
        return id;
    }

    @Override
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
