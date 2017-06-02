package fr.zhj2074.backoffice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BackofficeForbiddenException extends RuntimeException {

    public BackofficeForbiddenException(Throwable cause) {
        super(cause);
    }

    public BackofficeForbiddenException(String message) {
        super(message);
    }

    public BackofficeForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

}
