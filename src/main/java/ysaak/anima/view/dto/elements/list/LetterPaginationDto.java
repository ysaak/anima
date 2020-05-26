package ysaak.anima.view.dto.elements.list;

public class LetterPaginationDto {
    private final String letter;
    private final boolean active;
    private final boolean current;

    public LetterPaginationDto(String letter, boolean active, boolean current) {
        this.letter = letter;
        this.active = active;
        this.current = current;
    }

    public String getLetter() {
        return letter;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCurrent() {
        return current;
    }
}
