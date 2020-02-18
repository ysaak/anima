package ysaak.anima.data;

public enum ElementType {
    ANIME("ANI", "animes", true, true)
    ;

    private final String dbCode;
    private final String pathName;
    private final boolean hasSubtype;
    private final boolean hasEpisodeList;

    ElementType(String dbCode, String pathName, boolean hasSubtype, boolean hasEpisodeList) {
        this.dbCode = dbCode;
        this.pathName = pathName;
        this.hasSubtype = hasSubtype;
        this.hasEpisodeList = hasEpisodeList;
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
}
