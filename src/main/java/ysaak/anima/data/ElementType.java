package ysaak.anima.data;

public enum ElementType {
    ANIME("ANI", true, true, "animes.index", "animes.view")
    ;

    private final String dbCode;
    private final boolean hasSubtype;
    private final boolean hasEpisodeList;
    private final String indexRoute;
    private final String viewRoute;

    ElementType(String dbCode, boolean hasSubtype, boolean hasEpisodeList, String indexRoute, String viewRoute) {
        this.dbCode = dbCode;
        this.hasSubtype = hasSubtype;
        this.hasEpisodeList = hasEpisodeList;
        this.indexRoute = indexRoute;
        this.viewRoute = viewRoute;
    }

    public String getDbCode() {
        return dbCode;
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

    public String getViewRoute() {
        return viewRoute;
    }
}
