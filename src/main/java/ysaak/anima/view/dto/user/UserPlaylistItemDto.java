package ysaak.anima.view.dto.user;

import java.time.LocalDate;

public class UserPlaylistItemDto {
    private final String elementId;
    private final String elementName;
    private final String status;
    private final LocalDate startDate;

    private final Integer currentEpisode;
    private final Integer totalEpisodes;

    public UserPlaylistItemDto(String elementId, String elementName, String status, LocalDate startDate, Integer currentEpisode, Integer totalEpisodes) {
        this.elementId = elementId;
        this.elementName = elementName;
        this.status = status;
        this.startDate = startDate;
        this.currentEpisode = currentEpisode;
        this.totalEpisodes = totalEpisodes;
    }

    public String getElementId() {
        return elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Integer getCurrentEpisode() {
        return currentEpisode;
    }

    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }
}
