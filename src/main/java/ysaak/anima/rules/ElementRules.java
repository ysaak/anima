package ysaak.anima.rules;

import ysaak.anima.data.Element;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.utils.Validate;

public final class ElementRules {
    private ElementRules() { /**/ }

    /**
     * Validate a Element object
     * @param element Element to validate
     * @throws FunctionalException Thrown if the object is invalid
     */
    public static void validate(Element element) throws FunctionalException {
        Validate.notNull(element, "element");
    }

    public static void validateSeason(Season season) throws FunctionalException {

    }

    public static void validateEpisode(Episode episode) throws FunctionalException {

    }
}
