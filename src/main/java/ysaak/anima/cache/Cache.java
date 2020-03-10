package ysaak.anima.cache;

public enum Cache {
    ANIDB_ANIME_DATA(86_400_000)
    ;

    private final long ttl;

    Cache(long ttl) {
        this.ttl = ttl;
    }

    public long getTtl() {
        return ttl;
    }
}
