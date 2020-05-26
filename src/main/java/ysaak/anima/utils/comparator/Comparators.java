package ysaak.anima.utils.comparator;

import com.google.common.collect.ImmutableMap;
import ysaak.anima.data.Episode;
import ysaak.anima.data.RelationType;
import ysaak.anima.data.Season;
import ysaak.anima.utils.StringUtils;

import java.util.Comparator;
import java.util.Map;

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

    private static final Map<RelationType, Integer> RELATION_ORDER = ImmutableMap.<RelationType, Integer>builder()
        .put(RelationType.SEQUEL, 1)
        .put(RelationType.PREQUEL, 2)
        .put(RelationType.ALTERNATIVE_SETTING, 3)
        .put(RelationType.ALTERNATIVE_VERSION, 4)
        .put(RelationType.SUMMARY, 5)
        .put(RelationType.FULL_STORY, 6)
        .put(RelationType.SIDE_STORY, 7)
        .put(RelationType.SPIN_OFF, 8)
        .put(RelationType.PARENT_STORY, 9)
        .put(RelationType.ADAPTATION, 10)
        .put(RelationType.OTHER, 11)
        .build();

    public static Comparator<RelationType> relationType = Comparator.comparingInt(RELATION_ORDER::get);
}
