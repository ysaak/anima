package ysaak.anima.data;

public enum ElementType {
    ANIME("ANI", true, true, true, "animes.index", "animes.view")
    ;

    private final String dbCode;
    private final boolean subtype;
    private final boolean episodeList;
    private final boolean playlistProgress;
    private final String indexRoute;
    private final String viewRoute;

    ElementType(String dbCode, boolean subtype, boolean episodeList, boolean playlistProgress, String indexRoute, String viewRoute) {
        this.dbCode = dbCode;
        this.subtype = subtype;
        this.episodeList = episodeList;
        this.playlistProgress = playlistProgress;
        this.indexRoute = indexRoute;
        this.viewRoute = viewRoute;
    }

    public String getDbCode() {
        return dbCode;
    }

    public boolean hasSubtype() {
        return subtype;
    }

    public boolean hasPlaylistProgress() {
        return playlistProgress;
    }

    public boolean hasEpisodeList() {
        return episodeList;
    }

    public String getIndexRoute() {
        return indexRoute;
    }

    public String getViewRoute() {
        return viewRoute;
    }
}
