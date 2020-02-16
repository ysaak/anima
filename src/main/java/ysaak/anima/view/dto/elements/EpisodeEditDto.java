package ysaak.anima.view.dto.elements;

public class EpisodeEditDto {
    private String id;
    private String elementId;
    private String seasonId;
    private String number;
    private String title;

    public EpisodeEditDto() {
    }

    public EpisodeEditDto(String id, String elementId, String seasonId, String number, String title) {
        this.id = id;
        this.elementId = elementId;
        this.seasonId = seasonId;
        this.number = number;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
