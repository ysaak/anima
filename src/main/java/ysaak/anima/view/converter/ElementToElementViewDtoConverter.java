package ysaak.anima.view.converter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Collection;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementRemoteId;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Relation;
import ysaak.anima.data.RelationType;
import ysaak.anima.data.Season;
import ysaak.anima.data.Tag;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.comparator.Comparators;
import ysaak.anima.utils.comparator.SeasonComparator;
import ysaak.anima.view.dto.elements.ElementViewDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Converter(from = Element.class, to = ElementViewDto.class)
public class ElementToElementViewDtoConverter extends AbstractConverter<Element, ElementViewDto> {
    @Override
    protected ElementViewDto safeConvert(Element object) {
        List<ElementViewDto.ElementSeasonDto> seasonList = CollectionUtils.getNotNull(object.getSeasonList())
            .stream()
            .sorted(new SeasonComparator())
            .map(this::convertSeason)
            .collect(Collectors.toList());

        List<String> tagList = CollectionUtils.getNotNull(object.getTagList())
            .stream()
            .map(this::convertTag)
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.toList());

        List<ElementViewDto.ElementRelationListDto> relationList = convertRelationList(object.getRelationList());

        List<ElementViewDto.ElementRemoteIdDto> remoteIdList = convertRemoteIdList(object.getRemoteIdList());

        List<ElementViewDto.ElementCollectionDto> collectionList = convertCollectionList(object.getCollectionList());

        return new ElementViewDto(
            object.getId(),
            object.getTitle(),
            object.getType().name(),
            object.getSubType().name(),
            object.getReleaseYear(),
            object.getEpisodeCount(),
            object.getSynopsis(),
            seasonList,
            tagList,
            relationList,
            remoteIdList,
            collectionList
        );
    }

    private ElementViewDto.ElementSeasonDto convertSeason(Season season) {
        List<ElementViewDto.ElementEpisodeDto> episodeSet = CollectionUtils.getNotNull(season.getEpisodeList())
            .stream()
            .sorted()
            .map(this::convertEpisode)
            .collect(Collectors.toList());

        return new ElementViewDto.ElementSeasonDto(
            season.getId(),
            season.getNumber(),
            season.getTitle(),
            episodeSet
        );
    }

    private ElementViewDto.ElementEpisodeDto convertEpisode(Episode episode) {
        return new ElementViewDto.ElementEpisodeDto(
            episode.getId(),
            episode.getNumber(),
            episode.getTitle()
        );
    }

    private String convertTag(Tag tag) {
        return tag.getName();
    }

    private List<ElementViewDto.ElementRelationListDto> convertRelationList(final List<Relation> relationList) {
        final List<ElementViewDto.ElementRelationListDto> dtoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(relationList)) {
            final Multimap<RelationType, ElementViewDto.ElementRelationDto> relationMap = ArrayListMultimap.create();

            for (Relation relation : relationList) {
                relationMap.put(relation.getType(), convertRelation(relation.getId(), relation.getRelatedElement()));
            }

            for (RelationType type : relationMap.keySet()) {
                final List<ElementViewDto.ElementRelationDto> relatedElementList = new ArrayList<>(relationMap.get(type));
                relatedElementList.sort(Comparator.comparing(ElementViewDto.ElementRelationDto::getTitle));

                final ElementViewDto.ElementRelationListDto dto = new ElementViewDto.ElementRelationListDto(
                    type,
                    type.name(), //translationService.get("elements.relation." + type.name()),
                    relatedElementList
                );

                dtoList.add(dto);
            }

            dtoList.sort((o1, o2) -> Comparators.relationType.compare(o1.getRelationType(), o2.getRelationType()));
        }

        return dtoList;
    }

    private ElementViewDto.ElementRelationDto convertRelation(String relationId, Element relatedElement) {
        return new ElementViewDto.ElementRelationDto(
            relationId,
            relatedElement.getId(),
            relatedElement.getTitle()
        );
    }

    private List<ElementViewDto.ElementRemoteIdDto> convertRemoteIdList(List<ElementRemoteId> remoteIdList) {
        final List<ElementViewDto.ElementRemoteIdDto> dtoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(remoteIdList)) {
            remoteIdList.stream().map(this::convertRemoteId).forEach(dtoList::add);
        }

        return dtoList;
    }

    private ElementViewDto.ElementRemoteIdDto convertRemoteId(ElementRemoteId remoteId) {
        return new ElementViewDto.ElementRemoteIdDto(
            remoteId.getId(),
            remoteId.getExternalSite().getSiteName(),
            String.format(remoteId.getExternalSite().getUrlTemplate(), remoteId.getRemoteId())
        );
    }

    private List<ElementViewDto.ElementCollectionDto> convertCollectionList(List<Collection> collectionList) {
        final List<ElementViewDto.ElementCollectionDto> dtoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(collectionList)) {
            collectionList.stream().map(this::convertCollection).forEach(dtoList::add);
        }

        return dtoList;
    }

    private ElementViewDto.ElementCollectionDto convertCollection(Collection collection) {
        return new ElementViewDto.ElementCollectionDto(
            collection.getId(),
            collection.getName()
        );
    }
}
