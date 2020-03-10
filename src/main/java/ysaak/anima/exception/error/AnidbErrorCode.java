package ysaak.anima.exception.error;

import ysaak.anima.exception.ErrorCode;

public enum AnidbErrorCode implements ErrorCode {
    TITLE_LIST_TEMP_FILE_CREATION("ANIDB-ATL-001", "Error while creating temporary file"),
    TITLE_LIST_INVALID_SERVER_RESPONSE("ANIDB-ATL-002", "Invalid HTTP status (%d) while requesting file from server"),
    TITLE_LIST_FILE_DOWNLOAD("ANIDB-ATL-003", "Error while downloading title file from AniDB server"),
    TITLE_LIST_FILE_READ("ANIDB-ATL-004", "Error while extracting title from file"),

    IMPORT_ALREADY_IMPORTED("ANIDB-IMP-001", "Anime with id %s is already imported"),
    IMPORT_XML_READ("ANIDB-IMP-002", "Error while reading anime XML data"),
    IMPORT_API_ERROR("ANIDB-IMP-003", "API error : %s"),

    IMPORT_API_URI_CREATE("ANIDB-API-001", "Error while creating API uri"),
    IMPORT_API_INVALID_SERVER_RESPONSE("ANIDB-API-002", "Error while executing request %s. HTTP status: %d"),
    IMPORT_API_DATA_READ("ANIDB-API-003", "Error while reading API data for request %s")
    ;

    private final String code;
    private final String message;

    AnidbErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
