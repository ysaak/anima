package ysaak.anima.view.dto.elements;

import ysaak.anima.data.RelationType;

import java.util.List;

public class ElementViewDto {
    private final String id;

    private final String title;

    private final String type;

    private final String subType;

    private final int releaseYear;

    private final Integer episodeCount;

    private final String synopsis;

    private final List<ElementSeasonDto> seasonList;

    private final List<String> tagList;

    private final List<ElementRelationListDto> relationList;

    private final List<ElementRemoteIdDto> remoteIdList;

    private final List<ElementCollectionDto> collectionList;

    public ElementViewDto(String id, String title, String type, String subType, int releaseYear, Integer episodeCount, String synopsis, List<ElementSeasonDto> seasonList, List<String> tagList, List<ElementRelationListDto> relationList, List<ElementRemoteIdDto> remoteIdList, List<ElementCollectionDto> collectionList) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.subType = subType;
        this.releaseYear = releaseYear;
        this.episodeCount = episodeCount;
        this.synopsis = synopsis;
        this.seasonList = seasonList;
        this.tagList = tagList;
        this.relationList = relationList;
        this.remoteIdList = remoteIdList;
        this.collectionList = collectionList;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public List<ElementSeasonDto> getSeasonList() {
        return seasonList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public List<ElementRelationListDto> getRelationList() {
        return relationList;
    }

    public List<ElementRemoteIdDto> getRemoteIdList() {
        return remoteIdList;
    }

    public List<ElementCollectionDto> getCollectionList() {
        return collectionList;
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

    public static class ElementRelationListDto {
        private final RelationType relationType;
        private final String name;
        private final List<ElementRelationDto> elementList;

        public ElementRelationListDto(RelationType relationType, String name, List<ElementRelationDto> elementList) {
            this.relationType = relationType;
            this.name = name;
            this.elementList = elementList;
        }

        public RelationType getRelationType() {
            return relationType;
        }

        public String getName() {
            return name;
        }

        public List<ElementRelationDto> getElementList() {
            return elementList;
        }
    }

    public static class ElementRelationDto {
        private final String id;
        private final String title;
        private final String url;

        public ElementRelationDto(String id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class ElementRemoteIdDto {
        private final String id;
        private final String siteName;
        private final String url;

        public ElementRemoteIdDto(String id, String siteName, String url) {
            this.id = id;
            this.siteName = siteName;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public String getSiteName() {
            return siteName;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class ElementCollectionDto {
        private final String id;
        private final String name;

        public ElementCollectionDto(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
