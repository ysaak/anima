package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ysaak.anima.data.Anime;
import ysaak.anima.dto.view.anime.AnimeListDto;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.AnidbService;
import ysaak.anima.service.AnimeService;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.router.NamedRoute;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/animes")
public class AnimeController {

    private final AnimeService animeService;
    private final AnidbService anidbService;
    private final RoutingService routingService;

    @Autowired
    public AnimeController(AnimeService animeService, AnidbService anidbService, RoutingService routingService) {
        this.animeService = animeService;
        this.anidbService = anidbService;
        this.routingService = routingService;
    }

    @NamedRoute("animes.index")
    @GetMapping("/")
    public String indexAction(ModelMap model) {

        List<AnimeListDto.Anime> animeList =  this.animeService.findAll().stream().map(this::mapFromAnime).collect(Collectors.toList());
        //AnimeListDto animeListDto = new AnimeListDto(animeList);

        model.put("animeList", animeList);

        return "animes/index";
    }

    private AnimeListDto.Anime mapFromAnime(Anime anime) {

        String viewUrl = routingService.getUrlFor("animes.view", Collections.singletonMap("id", anime.getId())).orElse(null);

        return new AnimeListDto.Anime(
                anime.getTitle(),
                viewUrl
        );
    }

    @NamedRoute("animes.view")
    @GetMapping("/{id}")
    public String viewAction(ModelMap model, @PathVariable("id") String id) throws NoDataFoundException {
        Anime anime = this.animeService.findById(id).orElseThrow(() -> new NoDataFoundException("Bye bye"));
        model.put("anime", anime);


        String anidbLink = (StringUtils.isNotEmpty(anime.getAnidbId())) ? anidbService.getUrl(anime.getAnidbId()) : null;
        model.put("anidbLink", anidbLink);

        return "animes/view";
    }

}
