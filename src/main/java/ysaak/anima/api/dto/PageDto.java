package ysaak.anima.api.dto;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class PageDto<T> {
    private List<T> content;

    private int pageIndex;
    private int pageCount;

    public PageDto() {
    }

    public PageDto(List<T> content, int pageIndex, int pageCount) {
        this.content = content;
        this.pageIndex = pageIndex;
        this.pageCount = pageCount;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto<?> pageDto = (PageDto<?>) o;
        return pageIndex == pageDto.pageIndex &&
                pageCount == pageDto.pageCount &&
                Objects.equals(content, pageDto.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, pageIndex, pageCount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PageDto.class.getSimpleName() + "[", "]")
                .add("content=" + content)
                .add("pageIndex=" + pageIndex)
                .add("pageCount=" + pageCount)
                .toString();
    }
}
