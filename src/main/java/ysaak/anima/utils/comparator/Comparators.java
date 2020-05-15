package ysaak.anima.utils.comparator;

import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.utils.StringUtils;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Season> season = Comparator.comparingInt(Season::getNumber);

    public static Comparator<Episode> episode = (e1, e2) -> {
        String o1 = StringUtils.getNotNull(e1.getNumber());
        String o2 = StringUtils.getNotNull(e2.getNumber());

        String o1StringPart = o1.replaceAll("\\d", "");
        String o2StringPart = o2.replaceAll("\\d", "");

        if (o1StringPart.equalsIgnoreCase(o2StringPart)) {
            return StringUtils.extractDigits(o1) - StringUtils.extractDigits(o2);
        }
        return o1.compareTo(o2);
    };
}
