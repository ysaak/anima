package ysaak.anima.dto.view.anime;

import java.util.List;

public class AnimeListDto {

    public final List<Anime> animeList;

    public AnimeListDto(List<Anime> animeList) {
        this.animeList = animeList;
    }

    public static class Anime {
        public final String title;
        public final String viewUrl;

        public Anime(String title, String viewUrl) {
            this.title = title;
            this.viewUrl = viewUrl;
        }
    }
}
