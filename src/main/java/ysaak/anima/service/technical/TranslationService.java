package ysaak.anima.service.technical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ysaak.anima.exception.ErrorCode;

@Service
public class TranslationService {
    private final Logger LOGGER = LoggerFactory.getLogger(TranslationService.class);

    private static final String NULL_VALUE = "(null)";

    private final MessageSource messageSource;

    @Autowired
    public TranslationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key) {
        return getTranslation(key, null);
    }

    public String get(String key, Object...args) {
        return getTranslation(key, args);
    }

    public String get(Enum<?> enumKey) {
        if (enumKey == null) {
            return NULL_VALUE;
        }

        final String key = enumKey.getClass().getName() + "." + enumKey.name();
        return getTranslation(key, null);
    }

    public String translateError(ErrorCode errorCode, Object[] args) {
        if (errorCode == null) {
            return NULL_VALUE;
        }

        final String key = "error." + errorCode.getCode();
        return getTranslation(key, args);
    }

    private String getTranslation(String key, Object[] args) {
        if (key == null) {
            return NULL_VALUE;
        }

        try {
            return this.messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        }
        catch (NoSuchMessageException e) {
            LOGGER.error(e.getMessage());
            return "!!" + key + "!!";
        }
    }
}
