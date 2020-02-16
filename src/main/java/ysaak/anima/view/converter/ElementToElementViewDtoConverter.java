package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Element;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.view.dto.elements.ElementViewDto;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(from = Element.class, to = ElementViewDto.class)
public class ElementToElementViewDtoConverter extends AbstractConverter<Element, ElementViewDto> {

    @Override
    protected ElementViewDto safeConvert(Element object) {
        Set<ElementViewDto.ElementSeasonDto> seasonSet = CollectionUtils.getNotNull(object.getSeasonSet())
                .stream()
                .map(this::convertSeason)
                .sorted(Comparator.comparing(ElementViewDto.ElementSeasonDto::getNumber))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new ElementViewDto(
            object.getId(),
            fromEnum(object.getType()),
            object.getTitle(),
            fromEnum(object.getSubType()),
            object.getReleaseYear(),
            object.getSynopsis(),
            seasonSet
        );
    }

    private ElementViewDto.ElementSeasonDto convertSeason(Season season) {
        Set<ElementViewDto.ElementEpisodeDto> episodeSet = CollectionUtils.getNotNull(season.getEpisodeSet())
                .stream()
                .sorted()
                .map(this::convertEpisode)
                .collect(Collectors.toCollection(LinkedHashSet::new));

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

}
