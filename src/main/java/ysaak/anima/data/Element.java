package ysaak.anima.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ELEMENT")
public class Element {

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

    public Element() {
    }

    public Element(String id, String title, ElementType type, ElementSubType subType, int releaseYear, String synopsis) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.subType = subType;
        this.releaseYear = releaseYear;
        this.synopsis = synopsis;
    }

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
}
