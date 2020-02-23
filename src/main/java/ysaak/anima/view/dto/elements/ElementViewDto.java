package ysaak.anima.view.dto.elements;

import java.util.List;

public class ElementViewDto {
    private String id;

    private String title;

    private String type;

    private String subType;

    private int releaseYear;

    private String synopsis;

    private List<ElementSeasonDto> seasonList;

    private List<String> tagList;

    public ElementViewDto(String id, String title, String type, String subType, int releaseYear, String synopsis, List<ElementSeasonDto> seasonList, List<String> tagList) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.subType = subType;
        this.releaseYear = releaseYear;
        this.synopsis = synopsis;
        this.seasonList = seasonList;
        this.tagList = tagList;
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

    public List<ElementSeasonDto> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<ElementSeasonDto> seasonList) {
        this.seasonList = seasonList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public static class ElementSeasonDto {
        private String id;
        private int number;
        private String title;
        private List<ElementEpisodeDto> episodeList;

        public ElementSeasonDto(String id, int number, String title, List<ElementEpisodeDto> episodeList) {
            this.id = id;
            this.number = number;
            this.title = title;
            this.episodeList = episodeList;
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

        public List<ElementEpisodeDto> getEpisodeList() {
            return episodeList;
        }

        public void setEpisodeList(List<ElementEpisodeDto> episodeList) {
            this.episodeList = episodeList;
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
