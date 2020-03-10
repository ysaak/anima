package ysaak.anima.view.dto.importer;

public class ImportSearchResultDto {
    private final String externalId;
    private final String title;
    private final String remoteUrl;
    private final String elementId;

    public ImportSearchResultDto(String externalId, String title, String remoteUrl, String elementId) {
        this.externalId = externalId;
        this.title = title;
        this.remoteUrl = remoteUrl;
        this.elementId = elementId;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getTitle() {
        return title;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getElementId() {
        return elementId;
    }
}
