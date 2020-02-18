package ysaak.anima.view.dto.elements;

public class SeasonEditDto {
    private String id;
    private String elementId;
    private String title;

    public SeasonEditDto(String id, String elementId, String title) {
        this.id = id;
        this.elementId = elementId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
