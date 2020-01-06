package ysaak.anima.dao.converter.anime;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Anime;
import ysaak.anima.data.AnimeEpisode;
import ysaak.anima.dao.model.AnimeEpisodeModel;
import ysaak.anima.dao.model.AnimeModel;
import ysaak.anima.utils.CollectionUtils;

import java.util.stream.Collectors;

@Converter(from = AnimeModel.class, to = Anime.class)
public class AnimeModelToEntityConverter extends AbstractConverter<AnimeModel, Anime> {
    @Override
    public Anime safeConvert(AnimeModel animeModel) {
        Anime anime = new Anime(
                animeModel.getId(),
                animeModel.getTitle(),
                animeModel.getType(),
                animeModel.getStartDate(),
                animeModel.getEndDate(),
                animeModel.getSynopsis(),
                animeModel.getAnidbId()
        );

        if (CollectionUtils.isNotEmpty(animeModel.getEpisodeList())) {
            anime.setEpisodeList(
                    animeModel.getEpisodeList().stream()
                            .map(this::convertEpisodeModelToEntity)
                            .collect(Collectors.toList()));
        }

        return anime;
    }

    private AnimeEpisode convertEpisodeModelToEntity(AnimeEpisodeModel episode) {
        return new AnimeEpisode(
                episode.getNumber(),
                episode.getTitle()
        );
    }
}
