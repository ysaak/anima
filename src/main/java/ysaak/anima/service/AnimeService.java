package ysaak.anima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.converter.ConverterService;
import ysaak.anima.data.Anime;
import ysaak.anima.dao.model.AnimeModel;
import ysaak.anima.dao.repository.AnimeRepository;

import java.util.Optional;

@Service
public class AnimeService extends AbstractCrudService<Anime, AnimeModel, AnimeRepository> {

    @Autowired
    public AnimeService(ConverterService converterService, AnimeRepository animeRepository) {
        super(converterService, animeRepository);
    }

    public Optional<Anime> findByAnidbId(String anidbId) {
        Optional<AnimeModel> animeModel = repository.findByAnidbId(anidbId);
        return animeModel.map(m -> converterService.convert(m, Anime.class));
    }
}
