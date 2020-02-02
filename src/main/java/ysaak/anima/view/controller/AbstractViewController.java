package ysaak.anima.view.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.view.ViewConstants;

import java.util.Collections;
import java.util.List;

public abstract class AbstractViewController {

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