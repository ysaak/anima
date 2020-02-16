package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ysaak.anima.data.Element;
import ysaak.anima.dto.view.entity.AnimeListDto;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.ElementService;
import ysaak.anima.view.dto.elements.ElementViewDto;
import ysaak.anima.view.router.RoutingService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping(value = { "/animes", "/book" })
public class AnimeController extends AbstractViewController {

    private final ElementService elementService;
    private final RoutingService routingService;

    @Autowired
    public AnimeController(ElementService elementService, RoutingService routingService) {
        this.elementService = elementService;
        this.routingService = routingService;
    }

    @GetMapping("/")
    public String indexAction(ModelMap model) {

        List<AnimeListDto.Anime> animeList =  this.elementService.findAll().stream()
                .map(this::mapFromAnime)
                .collect(Collectors.toList());

        model.put("elementList", animeList);

        return "elements/index";
    }

    private AnimeListDto.Anime mapFromAnime(Element element) {

        String viewUrl = routingService.getElementPath(element);

        return new AnimeListDto.Anime(
                element.getTitle(),
                viewUrl
        );
    }

    @GetMapping("/{id}")
    public String viewAction(ModelMap model, @PathVariable("id") String id) throws NoDataFoundException {
        Element element = this.elementService.findById(id);


        ElementViewDto elementView = converters().convert(element, ElementViewDto.class);

        model.put("element", elementView);

        int seasonCount = elementView.getSeasonSet().size();
        model.put("seasonCount", seasonCount);


//        String anidbLink = (StringUtils.isNotEmpty(anime.getAnidbId())) ? oldAnidbService.getUrl(anime.getAnidbId()) : null;
//        model.put("anidbLink", anidbLink);

        return "elements/view";
    }
}
