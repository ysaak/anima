package ysaak.anima.data;

import java.time.LocalDate;
import java.util.List;

public class Anime implements Entity {

    private String id;

    private String title;

    private AnimeType type;

    private LocalDate startDate;

    private LocalDate endDate;

    private String synopsis;

    private String anidbId;

    private List<AnimeEpisode> episodeList;

    public Anime() {
    }

    public Anime(String id, String title, AnimeType type, LocalDate startDate, LocalDate endDate, String synopsis, String anidbId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.synopsis = synopsis;
        this.anidbId = anidbId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    public List<AnimeEpisode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<AnimeEpisode> episodeList) {
        this.episodeList = episodeList;
    }
}
