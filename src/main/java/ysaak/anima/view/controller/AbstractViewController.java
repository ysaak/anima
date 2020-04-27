package ysaak.anima.view.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.exception.ErrorCode;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.ViewConstants;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractViewController implements IAnimaComponent {

    protected final TranslationService translationService;
    protected final RoutingService routingService;

    public AbstractViewController(TranslationService translationService, RoutingService routingService) {
        this.translationService = translationService;
        this.routingService = routingService;
    }

    protected ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    protected void handleFunctionalException(final RedirectAttributes redirectAttributes, FunctionalException exception) {
        ErrorCode errorCode = exception.getError();

        // Handle 404 error
        if (errorCode.getStatus() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(errorCode.getStatus(), errorCode.getMessage(), exception);
        }

        String translatedError = translationService.translateError(errorCode, exception.getArgs());
        addFlashErrorMessage(redirectAttributes, translatedError);
    }

    public String redirect(final String routeName) {
        return this.redirect(routeName, Collections.emptyMap());
    }

    public String redirect(final String routeName, final Map<String, Object> parameters) {
        return "redirect:" + routingService.getUrlFor(routeName, parameters);
    }

    protected void addFlashErrorMessage(final RedirectAttributes redirectAttributes, final String message) {
        addFlashErrorMessage(redirectAttributes, Collections.singletonList(message));
    }

    protected void addFlashErrorMessage(final RedirectAttributes redirectAttributes, final List<String> messageList) {
        redirectAttributes.addFlashAttribute(ViewConstants.FLASH_ERROR_ATTRIBUTE_KEY, messageList);
    }

    protected void addFlashInfoMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ViewConstants.FLASH_INFO_ATTRIBUTE_KEY, Collections.singletonList(message));
    }
}
