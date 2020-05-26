package ysaak.anima.view.dto.elements.list;

public class ElementListDto {
    private final String id;
    private final String title;

    public ElementListDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
