package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Element;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.comparator.SeasonComparator;
import ysaak.anima.view.dto.elements.ElementViewDto;

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

        return new ElementViewDto(
            object.getId(),
            object.getTitle(),
            fromEnum(object.getType()),
            fromEnum(object.getSubType()),
            object.getReleaseYear(),
            object.getSynopsis(),
            seasonList,
            tagList
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

    private String convertTag(TagModel tag) {
        return tag.getName();
    }

}
