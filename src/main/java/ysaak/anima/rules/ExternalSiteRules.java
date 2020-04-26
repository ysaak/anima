package ysaak.anima.rules;

import ysaak.anima.data.ExternalSite;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.ExternalSiteErrorCode;
import ysaak.anima.utils.Validate;

public final class ExternalSiteRules {
    private ExternalSiteRules() { /**/ }

    /**
     * Validate an ExternalSite object
     * @param externalSite External site to validate
     * @throws FunctionalException Thrown is the object is invalid
     */
    public static void validate(ExternalSite externalSite) throws FunctionalException {
        Validate.notNull(externalSite, "externalSite");
        Validate.length(externalSite.getCode(), 4, 30, ExternalSiteErrorCode.VALIDATE_CODE_FORMAT, 4, 30);
        Validate.length(externalSite.getSiteName(), 4, 30, ExternalSiteErrorCode.VALIDATE_SITE_NAME_FORMAT, 4, 30);
        Validate.notBlank(externalSite.getUrlTemplate(), ExternalSiteErrorCode.VALIDATE_URL_TEMPLATE_FORMAT);
        Validate.maxLength(externalSite.getUrlTemplate(), 250, ExternalSiteErrorCode.VALIDATE_URL_TEMPLATE_FORMAT);
        Validate.validUrl(externalSite.getUrlTemplate(), ExternalSiteErrorCode.VALIDATE_URL_TEMPLATE_FORMAT);
        Validate.isTrue(externalSite.getUrlTemplate().contains("%s"), ExternalSiteErrorCode.VALIDATE_URL_TEMPLATE_PLACEHOLDER);
    }
}
