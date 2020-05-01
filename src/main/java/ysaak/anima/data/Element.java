package ysaak.anima.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ELEMENT")
public class Element implements IEntity {

    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "ELEM_ID", nullable = false)
    private String id;

    @Column(name = "ELEM_TITLE", nullable = false)
    private String title;

    @Column(name = "ELEM_TYPE", nullable = false)
    private ElementType type;

    @Column(name = "ELEM_SUBTYPE", nullable = false)
    private ElementSubType subType;

    @Column(name = "ELEM_RELEASE_YEAR")
    private int releaseYear;

    @Column(name = "ELEM_SYNOPSIS", columnDefinition = "text")
    private String synopsis;

    @OneToMany(mappedBy = "element", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Season> seasonList;

    @ManyToMany
    @JoinTable(
            name = "L_ELEMENT_TAG",
            joinColumns = @JoinColumn(name = "ELEMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> tagList;

    @OneToMany(mappedBy = "element")
    private List<Relation> relationList;

    @OneToMany(mappedBy = "element", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementRemoteId> remoteIdList;

    @ManyToMany
    @JoinTable(
            name = "L_ELEMENT_COLLECTION",
            joinColumns = @JoinColumn(name = "ELEMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "COLLECTION_ID")
    )
    private List<Collection> collectionList;

    public Element() {
    }

    public Element(String id) {
        this.id = id;
    }

    public Element(String id, String title, ElementType type, ElementSubType subType, int releaseYear, String synopsis) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.subType = subType;
        this.releaseYear = releaseYear;
        this.synopsis = synopsis;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public ElementSubType getSubType() {
        return subType;
    }

    public void setSubType(ElementSubType subType) {
        this.subType = subType;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Relation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<Relation> relationList) {
        this.relationList = relationList;
    }

    public List<ElementRemoteId> getRemoteIdList() {
        return remoteIdList;
    }

    public void setRemoteIdList(List<ElementRemoteId> remoteIdList) {
        this.remoteIdList = remoteIdList;
    }

    public List<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }
}
