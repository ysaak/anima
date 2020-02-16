package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.comparator.SeasonComparator;
import ysaak.anima.view.dto.KeyValueItem;
import ysaak.anima.view.dto.elements.AddSeasonDto;
import ysaak.anima.view.dto.elements.ElementEditDto;
import ysaak.anima.view.dto.elements.EpisodeEditDto;
import ysaak.anima.view.router.NamedRoute;
import ysaak.anima.view.router.RoutingService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/elements")
@Transactional
public class ElementController extends AbstractViewController {

    private final ElementService elementService;
    private final RoutingService routingService;
    private final TranslationService translationService;

    @Autowired
    public ElementController(ElementService elementService, RoutingService routingService, TranslationService translationService) {
        this.elementService = elementService;
        this.routingService = routingService;
        this.translationService = translationService;
    }

    @GetMapping("/new")
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("element")) {
            model.put("element", new Element());
        }

        List<KeyValueItem> typeList = Stream.of(ElementType.ANIME).map(this::mapFromEnum).collect(Collectors.toList());
        model.put("typeList", typeList);

        List<KeyValueItem> subTypeList = Stream.of(ElementSubType.UNDETERMINED, ElementSubType.TV).map(this::mapFromEnum).collect(Collectors.toList());
        model.put("subTypeList", subTypeList);

        return "elements/edit";
    }

    @PostMapping("/")
    public String createAction(@ModelAttribute ElementEditDto elementEditDto, final RedirectAttributes redirectAttributes) {
        final Element elementToSave = converters().convert(elementEditDto, Element.class);
        final Element savedElement;

        try {
            savedElement = elementService.save(elementToSave);
        }
        catch (DataValidationException dve) {
            addFlashErrorMessage(redirectAttributes, dve.getMessageList());
            redirectAttributes.addFlashAttribute("element", elementEditDto);
            return "redirect:/elements/new";
        }

        return "redirect:" + routingService.getElementPath(savedElement);
    }

    @NamedRoute("elements.edit")
    @GetMapping("/{id}/edit")
    public String editAction(ModelMap model, @PathVariable("id") String id) throws Exception {
        if (!model.containsAttribute("element")) {
            final Element element = elementService.findById(id);

            final ElementEditDto elementEditDto = converters().convert(element, ElementEditDto.class);
            model.put("element", elementEditDto);
        }

        List<KeyValueItem> typeList = Stream.of(ElementType.ANIME).map(e -> new KeyValueItem(e.name(), e.name())).collect(Collectors.toList());
        model.put("typeList", typeList);

        List<KeyValueItem> subTypeList = Stream.of(ElementSubType.UNDETERMINED, ElementSubType.TV).map(e -> new KeyValueItem(e.name(), e.name())).collect(Collectors.toList());
        model.put("subTypeList", subTypeList);

        return "elements/edit";
    }

    private KeyValueItem mapFromEnum(Enum<?> item) {
        return new KeyValueItem(item.name(), translationService.get(item));
    }

    @PostMapping("/{id}")
    public String updateAction(@ModelAttribute ElementEditDto elementEditDto, final RedirectAttributes redirectAttributes) {
        final Element elementToSave = converters().convert(elementEditDto, Element.class);
        final Element savedElement;

        try {
            savedElement = elementService.save(elementToSave);
        }
        catch (DataValidationException dve) {
            addFlashErrorMessage(redirectAttributes, dve.getMessageList());
            redirectAttributes.addFlashAttribute("element", elementEditDto);
            return "redirect:/elements/" + elementEditDto.getId() + "/edit";
        }

        return "redirect:" + routingService.getElementPath(savedElement);
    }

    @PostMapping("/{id}/delete")
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        elementService.delete(id);
        addFlashInfoMessage(redirectAttributes, translationService.get("elements.delete"));

        return "redirect:/";
    }

    /* ----- Season management ----- */
    @PostMapping("/{id}/seasons/")
    public String seasonCreateAction(@ModelAttribute AddSeasonDto addSeasonDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;
        try {
            element = elementService.addSeason(addSeasonDto.getElementId(), addSeasonDto.getTitle());
        }
        catch (DataValidationException dve) {
            // FIXME revoir ce mécanisme
            addFlashErrorMessage(redirectAttributes, dve.getMessageList());
            return "redirect:/";
        }

        return "redirect:" + routingService.getElementPath(element);
    }

    @PostMapping("/{elementId}/seasons/{seasonId}/delete")
    public String seasonDeleteAction(@PathVariable("elementId") final String elementId, @PathVariable("seasonId") final String seasonId, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.deleteSeason(elementId, seasonId);
        addFlashInfoMessage(redirectAttributes, translationService.get("elements.season.delete"));
        return "redirect:" + routingService.getElementPath(element);
    }

    /* ----- Episode management ----- */
    @GetMapping("{id}/episodes/new")
    public String episodeNewAction(final ModelMap model, @PathVariable("id") final String elementId) throws NoDataFoundException {

        final Element element = elementService.findById(elementId);

        final EpisodeEditDto episodeEditDto = new EpisodeEditDto(null, element.getId(), null, null, null);
        model.put("episode", episodeEditDto);

        final List<KeyValueItem> seasonList = CollectionUtils.getNotNull(element.getSeasonSet())
                .stream()
                .sorted(new SeasonComparator())
                .map(s -> new KeyValueItem(s.getId(), s.getTitle()))
                .collect(Collectors.toList());
        model.put("seasonList", seasonList);

        return "elements/edit_episode";
    }

    @PostMapping("/{id}/episodes/")
    public String episodeCreateAction(@ModelAttribute EpisodeEditDto episodeEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;

        final Episode episode = new Episode(episodeEditDto.getId(), episodeEditDto.getNumber(), episodeEditDto.getTitle());

        try {
            element = elementService.addEpisode(episodeEditDto.getElementId(), episodeEditDto.getSeasonId(), episode);
        }
        catch (DataValidationException dve) {
            // FIXME revoir ce mécanisme
            addFlashErrorMessage(redirectAttributes, dve.getMessageList());
            return "redirect:/";
        }

        return "redirect:" + routingService.getElementPath(element);
    }

    @GetMapping("/{elementId}/episodes/{episodeId}/edit")
    public String episodeEditAction(ModelMap model, @PathVariable("elementId") String elementId, @PathVariable("episodeId") String episodeId) throws NoDataFoundException {

        final Element element = elementService.findById(elementId);

        final Episode episode = element.getSeasonSet()
                .stream()
                .map(Season::getEpisodeSet)
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

        final List<KeyValueItem> seasonList = CollectionUtils.getNotNull(element.getSeasonSet())
                .stream()
                .sorted(new SeasonComparator())
                .map(s -> new KeyValueItem(s.getId(), s.getTitle()))
                .collect(Collectors.toList());
        model.put("seasonList", seasonList);

        return "elements/edit_episode";
    }

    @PostMapping("/{elementId}/episodes/{episodeId}")
    public String episodeUpdateAction(@ModelAttribute EpisodeEditDto episodeEditDto, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element;

        final Episode episode = new Episode(episodeEditDto.getId(), episodeEditDto.getNumber(), episodeEditDto.getTitle());

        try {
            element = elementService.updateEpisode(episodeEditDto.getElementId(), episodeEditDto.getSeasonId(), episode);
        }
        catch (DataValidationException dve) {
            // FIXME revoir ce mécanisme
            addFlashErrorMessage(redirectAttributes, dve.getMessageList());
            return "redirect:/";
        }

        return "redirect:" + routingService.getElementPath(element);
    }

    @PostMapping("/{elementId}/episodes/{episodeId}/delete")
    public String episodeDeleteAction(@PathVariable("elementId") final String elementId, @PathVariable("episodeId") final String episodeId, final RedirectAttributes redirectAttributes) throws NoDataFoundException {
        final Element element = elementService.deleteEpisode(elementId, episodeId);
        addFlashInfoMessage(redirectAttributes, translationService.get("elements.episode.delete"));
        return "redirect:" + routingService.getElementPath(element);
    }
}
