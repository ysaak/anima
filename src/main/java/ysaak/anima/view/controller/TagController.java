package ysaak.anima.view.controller;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementTagCount;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Tag;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.TagService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.dto.TagDto;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping("/tags/")
public class TagController extends AbstractViewController {
    public static final String ROUTE_TAGS_INDEX = "tags.index";
    private static final String ROUTE_TAGS_VIEW = "tags.view";

    private final TagService tagService;
    private final ElementService elementService;

    @Autowired
    public TagController(final ElementService elementService, final RoutingService routingService, final TranslationService translationService, final TagService tagService) {
        super(translationService, routingService);
        this.elementService = elementService;
        this.tagService = tagService;
    }
    
    @GetMapping(path = "/", name = ROUTE_TAGS_INDEX)
    public String indexAction(final ModelMap model) {
        final List<ElementTagCount> countList = elementService.elementTagCountList();
        Table<String, ElementType, Integer> countTable = HashBasedTable.create();
        for (ElementTagCount count : countList) {
            countTable.put(count.getTagId(), count.getElementType(), count.getCount());
        }

        final List<TagDto> collectionList = tagService.findAll()
            .stream()
            .map(tag -> mapToTagDto(tag, countTable.row(tag.getId())))
            .sorted(Comparator.comparing(TagDto::getName))
            .collect(Collectors.toList());
        model.put("tagList", collectionList);

        model.put("typeList", Collections.singletonList(ElementType.ANIME));

        return "tags/index";
    }

    private TagDto mapToTagDto(final Tag tag, final Map<ElementType, Integer> countMap) {
        int elementCount = countMap.values().stream().mapToInt(v -> v).sum();

        return new TagDto(
            tag.getId(),
            tag.getName(),
            tag.getDescription(),
            countMap,
            elementCount
        );
    }

    @GetMapping(path = "/{id}", name = ROUTE_TAGS_VIEW)
    public String listAction(final ModelMap model, @PathVariable("id") final String id) {
        final Tag tag = tagService.findById(id).orElseThrow(this::notFound);
        model.put("tagName", tag.getName());

        final List<ElementListDto> elementList = elementService.findByTagId(tag.getId()).stream()
            .map(this::createViewDto)
            .sorted(Comparator.comparing(ElementListDto::getTitle))
            .collect(Collectors.toList());

        model.put("elementList", elementList);

        return "tags/list";
    }

    private ElementListDto createViewDto(Element element) {
        return new ElementListDto(
                element.getId(),
                element.getTitle()
        );
    }
}
