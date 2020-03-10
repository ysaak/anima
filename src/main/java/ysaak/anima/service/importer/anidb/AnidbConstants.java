package ysaak.anima.service.importer.anidb;

import java.util.Arrays;
import java.util.List;

public class AnidbConstants {
    public static final String ANIDB_SITE_CODE = "ANIDB";

    public static final String ANIDB_TITLE_FILE_URL = "http://anidb.net/api/anime-titles.dat.gz";

    public static final List<String> TITLE_LANG_PRIORITY = Arrays.asList("fr", "en", "x-jat", "jp");

    public static final String CLIENT_NAME = "adbhac";
    public static final String CLIENT_VERSION  = "1";
    public static final String CLIENT_BASE_URL = "http://api.anidb.net:9001/httpapi?";
}
