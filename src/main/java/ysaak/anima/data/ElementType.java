package ysaak.anima.data;

public enum ElementType {
    ANIME("ANI", "animes", true, true, "animes.index")
    ;

    private final String dbCode;
    private final String pathName;
    private final boolean hasSubtype;
    private final boolean hasEpisodeList;
    private final String indexRoute;

    ElementType(String dbCode, String pathName, boolean hasSubtype, boolean hasEpisodeList, String indexRoute) {
        this.dbCode = dbCode;
        this.pathName = pathName;
        this.hasSubtype = hasSubtype;
        this.hasEpisodeList = hasEpisodeList;
        this.indexRoute = indexRoute;
    }

    public String getDbCode() {
        return dbCode;
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

    public String getIndexRoute() {
        return indexRoute;
    }
}
