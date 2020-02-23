package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ysaak.anima.config.ElementConstants;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.StorageService;
import ysaak.anima.view.dto.elements.ElementViewDto;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.dto.elements.list.LetterPaginationDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping(value = { "/animes", "/book" })
public class AnimeController extends AbstractViewController {
    private final ElementService elementService;
    private final StorageService storageService;
    private final RoutingService routingService;

    @Autowired
    public AnimeController(ElementService elementService, StorageService storageService, RoutingService routingService) {
        this.elementService = elementService;
        this.storageService = storageService;
        this.routingService = routingService;
    }

    @GetMapping("/")
    public String indexAction(ModelMap model) {
        return byLetterAction(model, ElementConstants.NON_ALPHA_LETTER);
    }

    @GetMapping("/byLetter/{letter}")
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

    @GetMapping("/{id}")
    public String viewAction(ModelMap model, @PathVariable("id") String id) throws NoDataFoundException {
        Element element = this.elementService.findById(id);


        ElementViewDto elementView = converters().convert(element, ElementViewDto.class);

        model.put("element", elementView);

        int seasonCount = elementView.getSeasonList().size();
        model.put("seasonCount", seasonCount);

//        String anidbLink = (StringUtils.isNotEmpty(anime.getAnidbId())) ? oldAnidbService.getUrl(anime.getAnidbId()) : null;
//        model.put("anidbLink", anidbLink);

        return "elements/view";
    }
}
