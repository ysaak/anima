package ysaak.anima.rules;

import ysaak.anima.data.Tag;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.TagErrorCode;
import ysaak.anima.utils.Validate;

public final class TagRules {
    private TagRules() { /**/ }

    /**
     * Validate a Tag object
     * @param tag Tag to validate
     * @throws FunctionalException Thrown is the object is invalid
     */
    public static void validate(Tag tag) throws FunctionalException {
        Validate.notNull(tag, "tag");
        Validate.length(tag.getName(), 2, 255, TagErrorCode.VALIDATE_NAME_FORMAT, 2, 250);
        Validate.maxLength(tag.getDescription(), 4000, TagErrorCode.VALIDATE_DESCRIPTION_FORMAT, 4000);
    }
}
