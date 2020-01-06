package ysaak.anima.dao.model;

import org.hibernate.annotations.GenericGenerator;
import ysaak.anima.data.AnimeType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ANIME")
public class AnimeModel {

    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "ANIM_ID", nullable = false)
    private String id;

    @Column(name = "ANIM_TITLE", nullable = false)
    private String title;

    @Column(name = "ANIM_TYPE", nullable = false)
    private AnimeType type;

    @Column(name = "ANIM_START_DATE")
    private LocalDate startDate;

    @Column(name = "ANIM_END_DATE")
    private LocalDate endDate;

    @Column(name = "ANIM_SYNOPSIS", columnDefinition = "text")
    private String synopsis;

    @Column(name = "ANIM_ANIDB_ID")
    private String anidbId;

    @OneToMany(mappedBy="anime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimeEpisodeModel> episodeList;

    public AnimeModel() {
    }

    public AnimeModel(String id, String title, AnimeType type, LocalDate startDate, LocalDate endDate, String synopsis, String anidbId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.synopsis = synopsis;
        this.anidbId = anidbId;
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

    public AnimeType getType() {
        return type;
    }

    public void setType(AnimeType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAnidbId() {
        return anidbId;
    }

    public void setAnidbId(String anidbId) {
        this.anidbId = anidbId;
    }

    public List<AnimeEpisodeModel> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<AnimeEpisodeModel> episodeList) {
        this.episodeList = episodeList;
    }
}
