package ysaak.anima.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ysaak.anima.api.dto.AnidbImportParamDto;
import ysaak.anima.exception.anidbimport.AnimeAlreadyImportedException;
import ysaak.anima.data.Anime;
import ysaak.anima.service.AnidbService;
import ysaak.anima.service.AnimeService;

import java.util.Optional;

@RestController
@RequestMapping("/api/animes")
public class ApiAnimeController extends AbstractCrudController<Anime, AnimeService> {

    private final AnidbService anidbService;

    @Autowired
    public ApiAnimeController(AnimeService animeService, AnidbService anidbService) {
        super(animeService);
        this.anidbService = anidbService;
    }

    @PostMapping("/import")
    public ResponseEntity<Anime> importFromAnidb(@RequestBody AnidbImportParamDto paramDto) throws Exception {
        // check if already existing
        final Optional<Anime> existingAnime = entityService.findByAnidbId(paramDto.id);
        if (existingAnime.isPresent()) {
            throw new AnimeAlreadyImportedException("anidbid=" + paramDto.id);
        }

        final Anime anime = anidbService.importAnime(paramDto.id);
        return super.create(anime);
    }
}
