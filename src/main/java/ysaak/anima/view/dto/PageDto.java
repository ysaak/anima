package ysaak.anima.view.dto;

public class PageDto {
    private final int current;
    private final int total;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final long elementCount;

    public PageDto(int current, int total, boolean hasPrevious, boolean hasNext, long elementCount) {
        this.current = current;
        this.total = total;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.elementCount = elementCount;
    }

    public int getCurrent() {
        return current;
    }

    public int getTotal() {
        return total;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public long getElementCount() {
        return elementCount;
    }
}
