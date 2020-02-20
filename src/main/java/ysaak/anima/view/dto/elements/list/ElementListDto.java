package ysaak.anima.view.dto.elements.list;

public class ElementListDto {
    private final String id;
    private final String title;
    private final String viewUrl;

    public ElementListDto(String id, String title, String viewUrl) {
        this.id = id;
        this.title = title;
        this.viewUrl = viewUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getViewUrl() {
        return viewUrl;
    }
}
