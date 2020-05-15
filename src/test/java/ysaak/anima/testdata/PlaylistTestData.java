package ysaak.anima.testdata;

import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;

import java.util.Arrays;

public class PlaylistTestData {
    public static Element mockedElement() {
        Season season1 = new Season(1, "Season 1");
        season1.setId("S1");
        season1.setEpisodeList(Arrays.asList(
            new Episode("S1E2", "2", "Episode 2"),
            new Episode("S1E1", "1", "Episode 1")
        ));

        Season season2 = new Season(2, "Season 1");
        season2.setId("S2");
        season2.setEpisodeList(Arrays.asList(
            new Episode("S2E2", "4", "Episode 4"),
            new Episode("S2E1", "3", "Episode 3")
        ));

        Element element = new Element("mockId");
        element.setType(ElementType.ANIME);
        element.setSeasonList(Arrays.asList(
            season2, season1
        ));

        return element;
    }
}
