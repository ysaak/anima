package ysaak.anima.rules;

import ysaak.anima.data.User;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.UserErrorCode;
import ysaak.anima.utils.Validate;

public final class UserRules {
    private UserRules() { /**/ }

    /**
     * Validate a User object
     * @param user User to validate
     * @throws FunctionalException Thrown if the object is invalid
     */
    public static void validate(User user) throws FunctionalException {
        Validate.notNull(user, "user");
        Validate.length(user.getName(), 2, 250, UserErrorCode.VALIDATE_NAME_FORMAT, 2, 250);
    }
}
