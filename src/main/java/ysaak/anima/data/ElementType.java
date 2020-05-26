package ysaak.anima.data;

public enum ElementType {
    ANIME("ANI", true, true, true)
    ;

    private final String dbCode;
    private final boolean subtype;
    private final boolean episodeList;
    private final boolean playlistProgress;

    ElementType(String dbCode, boolean subtype, boolean episodeList, boolean playlistProgress) {
        this.dbCode = dbCode;
        this.subtype = subtype;
        this.episodeList = episodeList;
        this.playlistProgress = playlistProgress;
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
}
