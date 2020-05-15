package ysaak.anima.view.dto.elements.view;

import java.time.LocalDate;

public class ElementPlaylistDto {
    private final String status;
    private final LocalDate startDate;
    private final Integer currentEpisode;

    public ElementPlaylistDto(String status, LocalDate startDate, Integer currentEpisode) {
        this.status = status;
        this.startDate = startDate;
        this.currentEpisode = currentEpisode;
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
}
