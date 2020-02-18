package ysaak.anima.view.dto.elements;

public class EpisodeMassAddDto {
    private String elementId;
    private String seasonId;
    private String episodeList;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(String episodeList) {
        this.episodeList = episodeList;
    }
}
