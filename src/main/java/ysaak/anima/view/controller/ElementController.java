package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.RelationType;
import ysaak.anima.data.Season;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.data.storage.StorageFormat;
import ysaak.anima.data.storage.StorageType;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.CollectionService;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.ExternalSiteService;
import ysaak.anima.service.PlaylistService;
import ysaak.anima.service.RelationService;
import ysaak.anima.service.StorageService;
import ysaak.anima.service.TagService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.utils.comparator.SeasonComparator;
import ysaak.anima.view.dto.KeyValueItem;
import ysaak.anima.view.dto.elements.ElementEditDto;
import ysaak.anima.view.dto.elements.ElementViewDto;
import ysaak.anima.view.dto.elements.EpisodeEditDto;
import ysaak.anima.view.dto.elements.EpisodeMassAddDto;
import ysaak.anima.view.dto.elements.RelationAddDto;
import ysaak.anima.view.dto.elements.RemoteIdAddDto;
import ysaak.anima.view.dto.elements.SeasonEditDto;
import ysaak.anima.view.dto.elements.view.ElementPlaylistDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/elements")
@Transactional
public class ElementController extends AbstractViewController {
    public static final String ROUTE_VIEW = "elements.view";

    private final ElementService elementService;
    private final TagService tagService;
    private final StorageService storageService;
    private final RelationService relationService;
    private final ExternalSiteService externalSiteService;
    private final CollectionService collectionService;
    private final PlaylistService playlistService;

    @Autowired
    public ElementController(ElementService elementService, TagService tagService, StorageService storageService, RelationService relationService, ExternalSiteService externalSiteService, CollectionService collectionService, RoutingService routingService, TranslationService translationService, PlaylistService playlistService) {
        super(translationService, routingService);
        this.elementService = elementService;
        this.tagService = tagService;
        this.storageService = storageService;
        this.relationService = relationService;
        this.externalSiteService = externalSiteService;
        this.collectionService = collectionService;
        this.playlistService = playlistService;
    }

    @GetMapping(path = "/{id}", name = ROUTE_VIEW)
    public String viewAction(final ModelMap model, @PathVariable("id") final String id) {
        Element element = this.elementService.findById2(id).orElseThrow(this::notFound);

        ElementViewDto elementView = converters().convert(element, ElementViewDto.class);

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

    private ElementPlaylistDto convertToPlaylistDto(final PlaylistItem playlistItem) {
        return new ElementPlaylistDto(
            playlistItem.getStatus().name(),
            playlistItem.getStartDate(),
            playlistItem.getCurrentEpisode()
        );
    }

    @GetMapping(path = "/new", name = "elements.new")
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("element")) {
            model.put("element", new Element());
        }

        initComboList(model);

        return "elements/edit";
    }

    @PostMapping(path = "/", name = "elements.create")
    public String createAction(@ModelAttribute ElementEditDto elementEditDto, final RedirectAttributes redirectAttributes) {
        final Element elementToSave = converters().convert(elementEditDto, Element.class);
        final Element savedElement;

        try {
            savedElement = elementService.create(elementToSave);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("element", elementEditDto);
            return "redirect:/elements/new";
        }

        return "redirect:" + routingService.getUrlFor(savedElement);
    }

    @GetMapping(path = "/{id}/edit", name = "elements.edit")
    public String editAction(ModelMap model, @PathVariable("id") String id) throws Exception {
        if (!model.containsAttribute("element")) {
            final Element element = elementService.findById(id);

            final ElementEditDto elementEditDto = converters().convert(element, ElementEditDto.class);
            model.put("element", elementEditDto);
        }

        initComboList(model);

        return "elements/edit";
    }

    private void initComboList(final ModelMap model) {
        List<KeyValueItem> typeList = Stream.of(ElementType.ANIME)
            .map(this::enumToKeyValueItem)
            .collect(Collectors.toList());
        model.put("typeList", typeList);

        List<KeyValueItem> subTypeList = Stream.of(ElementSubType.UNDETERMINED, ElementSubType.TV, ElementSubType.MOVIE, ElementSubType.OVA, ElementSubType.SPECIAL)
            .map(this::enumToKeyValueItem)
            .collect(Collectors.toList());
        model.put("subTypeList", subTypeList);

        List<KeyValueItem> tagList = tagService.findAll().stream()
            .map(t -> new KeyValueItem(t.getId(), t.getName()))
            .sorted(Comparator.comparing(KeyValueItem::getValue))
            .collect(Collectors.toList());
        model.put("tagList", tagList);

        List<KeyValueItem> collectionList = collectionService.findAll().stream()
            .map(t -> new KeyValueItem(t.getId(), t.getName()))
            .sorted(Comparator.comparing(KeyValueItem::getValue))
            .collect(Collectors.toList());
        model.put("collectionList", collectionList);
    }

    private KeyValueItem enumToKeyValueItem(Enum<?> item) {
        return new KeyValueItem(item.name(), translationService.get(item));
    }

    @PostMapping(path = "/{id}", name = "elements.update")
    public String updateAction(@ModelAttribute ElementEditDto elementEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element elementToSave = converters().convert(elementEditDto, Element.class);
        final Element savedElement;

        try {
            savedElement = elementService.update(elementToSave);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("element", elementEditDto);
            return "redirect:/elements/" + elementEditDto.getId() + "/edit";
        }

        return "redirect:" + routingService.getUrlFor(savedElement);
    }

    @PostMapping(path = "/{id}/delete", name = "elements.delete")
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        elementService.delete(id);
        addFlashInfoMessage(redirectAttributes, translationService.get("elements.delete"));

        return "redirect:/";
    }

    /* ----- Image management ----- */

    @GetMapping("/{elementId}/image.png")
    @ResponseBody
    public ResponseEntity<Resource> getElementImage(@PathVariable("elementId") String elementId) throws FunctionalException {
        Resource file = storageService.getImage(StorageType.ELEMENT, StorageFormat.FULL, elementId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/{elementId}/thumbnail.png")
    @ResponseBody
    public ResponseEntity<Resource> getElementThumbnail(@PathVariable("elementId") String elementId) throws FunctionalException {
        Resource file = storageService.getImage(StorageType.ELEMENT, StorageFormat.THUMBNAIL, elementId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(path = "/{elementId}/image/edit", name = "elements.image.edit")
    public String editImageAction(final ModelMap model, @PathVariable("elementId") final String elementId) {
        model.put("elementId", elementId);
        return "elements/edit_image";
    }

    @PostMapping(path = "/{elementId}/image/", name = "elements.image.update")
    public String updateImageAction(@PathVariable("elementId") final String elementId, @RequestParam("file") final MultipartFile file) throws NoDataFoundException, FunctionalException {
        final Element element = elementService.findById(elementId);

        storageService.store(StorageType.ELEMENT, elementId, file);

        return "redirect:" + routingService.getUrlFor(element);
    }

    /* ----- Season management ----- */
    @GetMapping(path = "/{elementId}/seasons/new", name = "elements.seasons.new")
    public String seasonNewAction(final ModelMap model, @PathVariable("elementId") final String elementId) throws NoDataFoundException {
        final Element element = elementService.findById(elementId);

        final SeasonEditDto seasonEditDto = new SeasonEditDto(null, element.getId(), null);
        model.put("season", seasonEditDto);

        return "elements/edit_season";
    }

    @PostMapping(path = "/{elementId}/seasons/", name = "elements.seasons.create")
    public String seasonCreateAction(@ModelAttribute SeasonEditDto seasonEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;
        try {
            element = elementService.addSeason(seasonEditDto.getElementId(), seasonEditDto.getTitle());
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            return "redirect:/";
        }

        return "redirect:" + routingService.getUrlFor(element);
    }

    @GetMapping(path = "/{elementId}/seasons/{seasonId}/edit", name = "elements.seasons.edit")
    public String seasonEditAction(ModelMap model, @PathVariable("elementId") String elementId, @PathVariable("seasonId") String seasonId) throws NoDataFoundException {

        final Element element = elementService.findById(elementId);

        final Season season = element.getSeasonList()
                .stream()
                .filter(s -> s.getId().equals(seasonId))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("No season found with id " + seasonId + " in element " + elementId));

        final SeasonEditDto seasonEditDto = new SeasonEditDto(
                season.getId(),
                element.getId(),
                season.getTitle()
        );
        model.put("season", seasonEditDto);

        return "elements/edit_season";
    }

    @PostMapping(path = "/{elementId}/seasons/{seasonId}", name = "elements.seasons.update")
    public String seasonUpdateAction(@ModelAttribute SeasonEditDto seasonEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;

        element = elementService.updateSeasonTitle(seasonEditDto.getElementId(), seasonEditDto.getId(), seasonEditDto.getTitle());


        return "redirect:" + routingService.getUrlFor(element);
    }

    @PostMapping(path = "/{elementId}/seasons/{seasonId}/delete", name = "elements.seasons.delete")
    public String seasonDeleteAction(@PathVariable("elementId") final String elementId, @PathVariable("seasonId") final String seasonId, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.deleteSeason(elementId, seasonId);
        addFlashInfoMessage(redirectAttributes, translationService.get("elements.season.delete"));
        return "redirect:" + routingService.getUrlFor(element);
    }

    /* ----- Episode management ----- */
    @GetMapping(path = "/{elementId}/episodes/new", name = "elements.episodes.new")
    public String episodeNewAction(final ModelMap model, @PathVariable("elementId") final String elementId) throws NoDataFoundException {

        final Element element = elementService.findById(elementId);

        final EpisodeEditDto episodeEditDto = new EpisodeEditDto(null, element.getId(), null, null, null);
        model.put("episode", episodeEditDto);

        final List<KeyValueItem> seasonList = CollectionUtils.getNotNull(element.getSeasonList())
                .stream()
                .sorted(new SeasonComparator())
                .map(s -> new KeyValueItem(s.getId(), s.getTitle()))
                .collect(Collectors.toList());
        model.put("seasonList", seasonList);

        return "elements/edit_episode";
    }

    @PostMapping(path = "/{elementId}/episodes/", name = "elements.episodes.create")
    public String episodeCreateAction(@ModelAttribute EpisodeEditDto episodeEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;

        final Episode episode = new Episode(episodeEditDto.getId(), episodeEditDto.getNumber(), episodeEditDto.getTitle());

        try {
            element = elementService.addEpisode(episodeEditDto.getElementId(), episodeEditDto.getSeasonId(), episode);
        }
        catch (FunctionalException e) {
            // FIXME revoir ce mécanisme
            handleFunctionalException(redirectAttributes, e);
            return "redirect:/";
        }

        return "redirect:" + routingService.getUrlFor(element);
    }

    @GetMapping(path = "/{elementId}/episodes/massNew", name = "elements.episodes.mass-new")
    public String episodeMassNewAction(final ModelMap model, @PathVariable("elementId") final String elementId) throws NoDataFoundException {

        final Element element = elementService.findById(elementId);

        model.put("elementId", element.getId());

        final List<KeyValueItem> seasonList = CollectionUtils.getNotNull(element.getSeasonList())
                .stream()
                .sorted(new SeasonComparator())
                .map(s -> new KeyValueItem(s.getId(), s.getTitle()))
                .collect(Collectors.toList());
        model.put("seasonList", seasonList);

        return "elements/edit_episode_mass_add";
    }

    @PostMapping(path = "/{elementId}/episodes/massCreate", name = "elements.episodes.mass-create")
    public String episodeMassCreateAction(@ModelAttribute EpisodeMassAddDto episodeMassAddDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;

        if (StringUtils.isNotBlank(episodeMassAddDto.getEpisodeList())) {

            List<Episode> episodeList = new ArrayList<>();

            String[] lineArr = episodeMassAddDto.getEpisodeList().split("\n");

            for (String line : lineArr) {
                String[] episodeData = line.trim().split("\\|", 2);
                episodeList.add(new Episode(episodeData[0].trim(), episodeData[1].trim()));
            }

            try {
                element = elementService.addEpisode(episodeMassAddDto.getElementId(), episodeMassAddDto.getSeasonId(), episodeList);
            } catch (FunctionalException e) {
                handleFunctionalException(redirectAttributes, e);
                return "redirect:/";
            }
        }
        else {
            element = elementService.findById(episodeMassAddDto.getElementId());
        }

        return "redirect:" + routingService.getUrlFor(element);
    }

    @GetMapping(path = "/{elementId}/episodes/{episodeId}/edit", name = "elements.episodes.edit")
    public String episodeEditAction(ModelMap model, @PathVariable("elementId") String elementId, @PathVariable("episodeId") String episodeId) throws NoDataFoundException {

        final Element element = elementService.findById(elementId);

        final Episode episode = element.getSeasonList()
                .stream()
                .map(Season::getEpisodeList)
                .flatMap(Collection::stream)
                .filter(e -> e.getId().equals(episodeId))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("No episode found with id " + episodeId + " in element " + elementId));

        final EpisodeEditDto episodeEditDto = new EpisodeEditDto(
                episode.getId(),
                element.getId(),
                episode.getSeason().getId(),
                episode.getNumber(),
                episode.getTitle()
        );
        model.put("episode", episodeEditDto);

        final List<KeyValueItem> seasonList = CollectionUtils.getNotNull(element.getSeasonList())
                .stream()
                .sorted(new SeasonComparator())
                .map(s -> new KeyValueItem(s.getId(), s.getTitle()))
                .collect(Collectors.toList());
        model.put("seasonList", seasonList);

        return "elements/edit_episode";
    }

    @PostMapping(path = "/{elementId}/episodes/{episodeId}", name = "elements.episodes.update")
    public String episodeUpdateAction(@ModelAttribute EpisodeEditDto episodeEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;

        final Episode episode = new Episode(episodeEditDto.getId(), episodeEditDto.getNumber(), episodeEditDto.getTitle());

        try {
            element = elementService.updateEpisode(episodeEditDto.getElementId(), episodeEditDto.getSeasonId(), episode);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            return "redirect:/";
        }

        return "redirect:" + routingService.getUrlFor(element);
    }

    @PostMapping(path = "/{elementId}/episodes/{episodeId}/delete", name = "elements.episodes.delete")
    public String episodeDeleteAction(@PathVariable("elementId") final String elementId, @PathVariable("episodeId") final String episodeId, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.deleteEpisode(elementId, episodeId);
        addFlashInfoMessage(redirectAttributes, translationService.get("elements.episode.delete"));
        return "redirect:" + routingService.getUrlFor(element);
    }

    /* ----- Relation management ----- */
    @GetMapping(path = "/{elementId}/relations/new", name = "elements.relations.new")
    public String relationNewAction(final ModelMap model, @PathVariable("elementId") final String elementId) {

        model.put("elementId", elementId);

        final List<KeyValueItem> typeList = Stream.of(
                        RelationType.SEQUEL,
                        RelationType.PREQUEL,
                        RelationType.ALTERNATIVE_SETTING,
                        RelationType.ALTERNATIVE_VERSION,
                        RelationType.SUMMARY,
                        RelationType.FULL_STORY,
                        RelationType.SIDE_STORY,
                        RelationType.SPIN_OFF,
                        RelationType.PARENT_STORY,
                        RelationType.ADAPTATION,
                        RelationType.OTHER
                )
                .map(s -> new KeyValueItem(s.name(), translationService.get("elements.relation." + s.name())))
                .collect(Collectors.toList());
        model.put("typeList", typeList);

        return "elements/edit_relation";
    }

    @PostMapping(path = "/{elementId}/relations/", name = "elements.relations.create")
    public String relationCreateAction(@ModelAttribute RelationAddDto relationAddDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.findById(relationAddDto.getElementId());

        relationService.createRelation(relationAddDto.getElementId(), relationAddDto.getRelatedElementId(), relationAddDto.getType());

        return "redirect:" + routingService.getUrlFor(element);
    }

    @PostMapping(path = "/{elementId}/relations/{relationId}/delete", name = "elements.relations.delete")
    public String relationDeleteAction(@PathVariable("elementId") final String elementId, @PathVariable("relationId") final String relationId, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.findById(elementId);

        relationService.deleteRelation(relationId);

        addFlashInfoMessage(redirectAttributes, translationService.get("elements.relation.delete"));
        return "redirect:" + routingService.getUrlFor(element);
    }

    /* ----- Remote id management ----- */
    @GetMapping(path = "/{elementId}/remote-id/new", name = "elements.remote-ids.new")
    public String remoteIdNewAction(final ModelMap model, @PathVariable("elementId") final String elementId) {
        model.put("elementId", elementId);

        final List<KeyValueItem> siteList = externalSiteService.findAll().stream()
                .map(s -> new KeyValueItem(s.getId(), s.getSiteName()))
                .collect(Collectors.toList());
        model.put("siteList", siteList);

        return "elements/edit_remote_id";
    }

    @PostMapping(path = "/{elementId}/remote-id/", name = "elements.remote-ids.create")
    public String remoteIdCreateAction(@ModelAttribute RemoteIdAddDto remoteIdAddDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.findById(remoteIdAddDto.getElementId());

        try {
            elementService.addRemoteId(
                    remoteIdAddDto.getElementId(),
                    remoteIdAddDto.getExternalSiteId(),
                    remoteIdAddDto.getRemoteId()
            );
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
        }

        return "redirect:" + routingService.getUrlFor(element);
    }

    @PostMapping(path = "/{elementId}/remote-id/{remoteId}/delete", name = "elements.remote-ids.delete")
    public String remoteIdDeleteAction(final RedirectAttributes redirectAttributes, @PathVariable("elementId") final String elementId, @PathVariable("remoteId") final String remoteId) throws NoDataFoundException {
        final Element element = elementService.findById(elementId);
        elementService.deleteRemoteId(elementId, remoteId);

        this.addFlashInfoMessage(redirectAttributes, translationService.get("elements.remote-id.delete"));
        return "redirect:" + routingService.getUrlFor(element);
    }
}
