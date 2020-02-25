package ysaak.anima.view.controller;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ysaak.anima.config.ElementConstants;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Relation;
import ysaak.anima.data.RelationType;
import ysaak.anima.data.Season;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.comparator.SeasonComparator;
import ysaak.anima.view.dto.elements.ElementViewDto;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.dto.elements.list.LetterPaginationDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping(value = { "/animes", "/book" })
public class AnimeController extends AbstractViewController {
    private final ElementService elementService;
    private final RoutingService routingService;
    private final TranslationService translationService;

    @Autowired
    public AnimeController(final ElementService elementService, final RoutingService routingService, final TranslationService translationService) {
        this.elementService = elementService;
        this.routingService = routingService;
        this.translationService = translationService;
    }

    @GetMapping(path = "/", name = "animes.index")
    public String indexAction(ModelMap model) {
        return byLetterAction(model, ElementConstants.NON_ALPHA_LETTER);
    }

    @GetMapping(path = "/byLetter/{letter}", name = "animes.by-letter")
    public String byLetterAction(final ModelMap model, @PathVariable("letter") final String letter) {

        // Construct letter pagination
        final List<LetterPaginationDto> letterPaginationDtoList = new ArrayList<>();
        final List<String> usedLetterList = this.elementService.listUsedLetters();

        // Non alpha letters
        letterPaginationDtoList.add(createLetterPagination(ElementConstants.NON_ALPHA_LETTER, usedLetterList, letter));
        for (char c = 'A' ; c <= 'Z'; c++) {
            letterPaginationDtoList.add(createLetterPagination(Character.toString(c), usedLetterList, letter));
        }

        model.put("letterPaginationList", letterPaginationDtoList);

        List<ElementListDto> animeList =  this.elementService.findByTypeAndLetter(ElementType.ANIME, letter).stream()
                .map(this::createViewDto)
                .collect(Collectors.toList());

        model.put("elementList", animeList);

        return "elements/index";
    }

    private LetterPaginationDto createLetterPagination(String letter, List<String> usedLetterList, String currentLetter) {
        return new LetterPaginationDto(
                letter,
                MvcUriComponentsBuilder.fromMethodName(AnimeController.class, "byLetterAction", null, letter).toUriString(),
                usedLetterList.contains(letter),
                letter.equals(currentLetter)
        );
    }

    private ElementListDto createViewDto(Element element) {

        String viewUrl = routingService.getElementPath(element);

        return new ElementListDto(
                element.getId(),
                element.getTitle(),
                viewUrl
        );
    }

    @GetMapping(path = "/{id}", name = "animes.view")
    public String viewAction(ModelMap model, @PathVariable("id") String id) throws NoDataFoundException {
        Element element = this.elementService.findById(id);


        ElementViewDto elementView = convertElementToDto(element);

        model.put("element", elementView);

        int seasonCount = elementView.getSeasonList().size();
        model.put("seasonCount", seasonCount);

//        String anidbLink = (StringUtils.isNotEmpty(anime.getAnidbId())) ? oldAnidbService.getUrl(anime.getAnidbId()) : null;
//        model.put("anidbLink", anidbLink);

        return "elements/view";
    }

    private ElementViewDto convertElementToDto(Element object) {
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

        return new ElementViewDto(
                object.getId(),
                object.getTitle(),
                object.getType().name(),
                object.getSubType().name(),
                object.getReleaseYear(),
                object.getSynopsis(),
                seasonList,
                tagList,
                relationList
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
                        translationService.get("elements.relation." + type.name()),
                        relatedElementList
                );

                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    private ElementViewDto.ElementRelationDto convertRelation(String relationId, Element relatedElement) {
        final String url = routingService.getElementPath(relatedElement);

        return new ElementViewDto.ElementRelationDto(
                relationId,
                relatedElement.getTitle(),
                url
        );
    }
}
