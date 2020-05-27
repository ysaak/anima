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
import ysaak.anima.data.Collection;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementCollectionCount;
import ysaak.anima.data.ElementType;
import ysaak.anima.service.CollectionService;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.dto.collections.CollectionDto;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.router.RoutingService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping("/collections/")
public class CollectionController extends AbstractViewController {
    public static final String ROUTE_COLLECTIONS_INDEX = "collections.index";
    private static final String ROUTE_COLLECTIONS_VIEW = "collections.view";

    private final CollectionService collectionService;
    private final ElementService elementService;

    @Autowired
    public CollectionController(final ElementService elementService, final RoutingService routingService, final TranslationService translationService, CollectionService collectionService) {
        super(translationService, routingService);
        this.elementService = elementService;
        this.collectionService = collectionService;
    }
    
    @GetMapping(path = "/", name = ROUTE_COLLECTIONS_INDEX)
    public String indexAction(final ModelMap model) {
        final List<ElementCollectionCount> elementCollectionCountList = elementService.elementCollectionCountList();
        Table<String, ElementType, Integer> countTable = HashBasedTable.create();
        for (ElementCollectionCount count : elementCollectionCountList) {
            countTable.put(count.getCollectionId(), count.getElementType(), count.getCount());
        }

        final List<CollectionDto> collectionList = collectionService.findAll()
            .stream()
            .map(collection -> mapToCollectionDto(collection, countTable.row(collection.getId())))
            .sorted(Comparator.comparing(CollectionDto::getName))
            .collect(Collectors.toList());
        model.put("collectionList", collectionList);

        return "collections/index";
    }

    private CollectionDto mapToCollectionDto(final Collection collection, final Map<ElementType, Integer> countMap) {
        int elementCount = countMap.values().stream().mapToInt(v -> v).sum();

        return new CollectionDto(
            collection.getId(),
            collection.getName(),
            countMap,
            elementCount
        );
    }

    @GetMapping(path = "/{id}", name = ROUTE_COLLECTIONS_VIEW)
    public String listAction(final ModelMap model, @PathVariable("id") final String id) {

        final Collection collection = collectionService.findById(id).orElseThrow(this::notFound);
        model.put("collectionName", collection.getName());

        final List<ElementListDto> elementList = elementService.findByCollectionId(collection.getId()).stream()
            .map(this::createViewDto)
            .sorted(Comparator.comparing(ElementListDto::getTitle))
            .collect(Collectors.toList());

        model.put("elementList", elementList);

        return "collections/list";
    }

    private ElementListDto createViewDto(Element element) {
        return new ElementListDto(
                element.getId(),
                element.getTitle()
        );
    }
}
