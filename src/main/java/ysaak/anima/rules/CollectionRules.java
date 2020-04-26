package ysaak.anima.rules;

import ysaak.anima.data.Collection;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.CollectionErrorCode;
import ysaak.anima.utils.Validate;

public final class CollectionRules {
    private CollectionRules() { /**/ }

    /**
     * Validate a Collection  object
     * @param collection Collection to validate
     * @throws FunctionalException Thrown is the object is invalid
     */
    public static void validate(Collection collection) throws FunctionalException {
        Validate.notNull(collection, "collection");
        Validate.length(collection.getName(), 2, 255, CollectionErrorCode.VALIDATE_NAME_FORMAT, 2, 255);
    }
}
