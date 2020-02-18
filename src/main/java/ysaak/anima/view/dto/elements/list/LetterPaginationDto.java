package ysaak.anima.view.dto.elements.list;

public class LetterPaginationDto {
    private final String letter;
    private final String url;
    private final boolean active;
    private final boolean current;

    public LetterPaginationDto(String letter, String url, boolean active, boolean current) {
        this.letter = letter;
        this.url = url;
        this.active = active;
        this.current = current;
    }

    public String getLetter() {
        return letter;
    }

    public String getUrl() {
        return url;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCurrent() {
        return current;
    }
}
