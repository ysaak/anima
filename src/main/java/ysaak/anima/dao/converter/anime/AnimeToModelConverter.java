package ysaak.anima.dao.converter.anime;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Anime;
import ysaak.anima.data.AnimeEpisode;
import ysaak.anima.dao.model.AnimeEpisodeModel;
import ysaak.anima.dao.model.AnimeModel;
import ysaak.anima.utils.CollectionUtils;

import java.util.stream.Collectors;

@Converter(from = Anime.class, to = AnimeModel.class)
public class AnimeToModelConverter extends AbstractConverter<Anime, AnimeModel> {
    @Override
    public AnimeModel safeConvert(Anime anime) {
        AnimeModel animeModel = new AnimeModel(
                anime.getId(),
                anime.getTitle(),
                anime.getType(),
                anime.getStartDate(),
                anime.getEndDate(),
                anime.getSynopsis(),
                anime.getAnidbId()
        );

        if (CollectionUtils.isNotEmpty(anime.getEpisodeList())) {
            animeModel.setEpisodeList(
                    anime.getEpisodeList().stream()
                            .map(episode -> convertEpisodeToModel(animeModel, episode))
                            .collect(Collectors.toList()));
        }

        return animeModel;
    }

    private AnimeEpisodeModel convertEpisodeToModel(AnimeModel anime, AnimeEpisode episode) {
        return new AnimeEpisodeModel(
                anime,
                episode.getNumber(),
                episode.getTitle()
        );
    }
}
