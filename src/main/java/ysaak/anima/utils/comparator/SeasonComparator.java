package ysaak.anima.utils.comparator;

import ysaak.anima.data.Season;

import java.util.Comparator;

public class SeasonComparator implements Comparator<Season> {
    @Override
    public int compare(Season o1, Season o2) {
        return Integer.compare(o1.getNumber(), o2.getNumber());
    }
}
