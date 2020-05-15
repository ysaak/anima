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
import ysaak.anima.data.Collection;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementRemoteId;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Relation;
import ysaak.anima.data.RelationType;
import ysaak.anima.data.Season;
import ysaak.anima.data.Tag;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.PlaylistService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.comparator.SeasonComparator;
import ysaak.anima.view.dto.elements.ElementViewDto;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.dto.elements.list.LetterPaginationDto;
import ysaak.anima.view.dto.elements.view.ElementPlaylistDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping("/animes")
public class AnimeController extends AbstractViewController {
    private final ElementService elementService;
    private final RoutingService routingService;
    private final TranslationService translationService;
    private final PlaylistService playlistService;

    @Autowired
    public AnimeController(final ElementService elementService, final RoutingService routingService, final TranslationService translationService, PlaylistService playlistService) {
        super(translationService, routingService);
        this.elementService = elementService;
        this.routingService = routingService;
        this.translationService = translationService;
        this.playlistService = playlistService;
    }

    @GetMapping(path = "/", name = "animes.index")
    public String indexAction(ModelMap model) {
        return byLetterAction(model, null);
    }

    @GetMapping(path = "/byLetter/{letter}", name = "animes.by-letter")
    public String byLetterAction(final ModelMap model, @PathVariable("letter") final String letter) {

        final List<String> usedLetterList = this.elementService.listUsedLetters();

        // Requested letter null?
        final String requestedLetter;
        if (letter == null) {
            requestedLetter = usedLetterList.stream()
                    .sorted()
                    .findFirst()
                    .orElse(ElementConstants.NON_ALPHA_LETTER);
        }
        else {
            requestedLetter = letter;
        }

        // Construct letter pagination
        final List<LetterPaginationDto> letterPaginationDtoList = new ArrayList<>();
        letterPaginationDtoList.add(createLetterPagination(ElementConstants.NON_ALPHA_LETTER, usedLetterList, requestedLetter));
        for (char c = 'A' ; c <= 'Z'; c++) {
            letterPaginationDtoList.add(createLetterPagination(Character.toString(c), usedLetterList, requestedLetter));
        }

        model.put("letterPaginationList", letterPaginationDtoList);

        // Load elements
        List<ElementListDto> animeList =  this.elementService.findByTypeAndLetter(ElementType.ANIME, requestedLetter).stream()
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

        String viewUrl = routingService.getUrlFor(element);

        return new ElementListDto(
                element.getId(),
                element.getTitle(),
                viewUrl
        );
    }

    @GetMapping(path = "/{id}", name = "animes.view")
    public String viewAction(ModelMap model, @PathVariable("id") String id) {
        Element element = this.elementService.findById2(id).orElseThrow(this::notFound);

        ElementViewDto elementView = convertElementToDto(element);

        model.put("element", elementView);

        int seasonCount = elementView.getSeasonList().size();
        model.put("seasonCount", seasonCount);

        AuthenticationHolder.getAuthenticatedUserId().ifPresent(userId -> {
            final ElementPlaylistDto playlistDto = playlistService.getItem(userId, element.getId())
                .map(this::convertToPlaylistDto)
                .orElse(null);

            model.put("playlistItem", playlistDto);
        });
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
                        translationService.get("elements.relation." + type.name()),
                        relatedElementList
                );

                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    private ElementViewDto.ElementRelationDto convertRelation(String relationId, Element relatedElement) {
        final String url = routingService.getUrlFor(relatedElement);

        return new ElementViewDto.ElementRelationDto(
                relationId,
                relatedElement.getTitle(),
                url
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

    private ElementPlaylistDto convertToPlaylistDto(final PlaylistItem playlistItem) {
        return new ElementPlaylistDto(
            playlistItem.getStatus().name(),
            playlistItem.getStartDate(),
            playlistItem.getCurrentEpisode()
        );
    }
}
