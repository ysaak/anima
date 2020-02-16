package ysaak.anima.view.dto.elements;

import java.util.Set;

public class ElementViewDto {
    private String id;

    private String title;

    private String type;

    private String subType;

    private int releaseYear;

    private String synopsis;

    private Set<ElementSeasonDto> seasonSet;

    public ElementViewDto(String id, String title, String type, String subType, int releaseYear, String synopsis, Set<ElementSeasonDto> seasonSet) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.subType = subType;
        this.releaseYear = releaseYear;
        this.synopsis = synopsis;
        this.seasonSet = seasonSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Set<ElementSeasonDto> getSeasonSet() {
        return seasonSet;
    }

    public void setSeasonSet(Set<ElementSeasonDto> seasonSet) {
        this.seasonSet = seasonSet;
    }

    public static class ElementSeasonDto {
        private String id;
        private int number;
        private String title;
        private Set<ElementEpisodeDto> episodeSet;

        public ElementSeasonDto(String id, int number, String title, Set<ElementEpisodeDto> episodeSet) {
            this.id = id;
            this.number = number;
            this.title = title;
            this.episodeSet = episodeSet;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Set<ElementEpisodeDto> getEpisodeSet() {
            return episodeSet;
        }

        public void setEpisodeSet(Set<ElementEpisodeDto> episodeSet) {
            this.episodeSet = episodeSet;
        }
    }

    public static class ElementEpisodeDto {
        private String id;
        private String number;
        private String title;

        public ElementEpisodeDto(String id, String number, String title) {
            this.id = id;
            this.number = number;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
