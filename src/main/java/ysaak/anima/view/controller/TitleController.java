package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ysaak.anima.config.ElementConstants;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.dto.elements.list.LetterPaginationDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping("/titles/")
public class TitleController extends AbstractViewController {
    public static final String ROUTE_TITLES_INDEX = "titles.index";
    private static final String ROUTE_TITLES_LETTER = "titles.letter";

    private final ElementService elementService;

    @Autowired
    public TitleController(final ElementService elementService, final RoutingService routingService, final TranslationService translationService) {
        super(translationService, routingService);
        this.elementService = elementService;
    }
    
    @GetMapping(path = "/", name = ROUTE_TITLES_INDEX)
    public String indexAction(final ModelMap model) {
        return letterAction(model, null);
    }

    @GetMapping(path = "/{letter}", name = ROUTE_TITLES_LETTER)
    public String letterAction(final ModelMap model, @PathVariable(name = "letter", required = false) final String letter) {

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
        List<ElementListDto> elementDtoList =  this.elementService.findByTypeAndLetter(ElementType.ANIME, requestedLetter).stream()
            .map(this::createViewDto)
            .sorted(Comparator.comparing(ElementListDto::getTitle))
            .collect(Collectors.toList());

        model.put("elementList", elementDtoList);

        return "titles";
    }

    private LetterPaginationDto createLetterPagination(String letter, List<String> usedLetterList, String currentLetter) {
        return new LetterPaginationDto(
                letter,
                usedLetterList.contains(letter),
                letter.equals(currentLetter)
        );
    }

    private ElementListDto createViewDto(Element element) {
        return new ElementListDto(
                element.getId(),
                element.getTitle()
        );
    }
}
