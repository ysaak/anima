package ysaak.anima.data;

public enum ElementType {
    ANIME("animes", true, true)
    ;

    private final String pathName;
    private final boolean hasSubtype;
    private final boolean hasEpisodeList;

    ElementType(String pathName, boolean hasSubtype, boolean hasEpisodeList) {
        this.pathName = pathName;
        this.hasSubtype = hasSubtype;
        this.hasEpisodeList = hasEpisodeList;
    }

    public String getPathName() {
        return pathName;
    }

    public boolean isHasSubtype() {
        return hasSubtype;
    }

    public boolean isHasEpisodeList() {
        return hasEpisodeList;
    }
}
