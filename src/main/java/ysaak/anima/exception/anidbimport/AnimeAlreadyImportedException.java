package ysaak.anima.exception.anidbimport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AnimeAlreadyImportedException extends Exception {

    public AnimeAlreadyImportedException(String message) {
        super(message);
    }
}
