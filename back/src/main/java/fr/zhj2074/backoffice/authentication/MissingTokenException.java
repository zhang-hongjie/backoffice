package fr.zhj2074.backoffice.authentication;

import fr.zhj2074.backoffice.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized: missing Token")
public class MissingTokenException extends AppException {
    public MissingTokenException(String msg) {
        super(msg);
    }

    public MissingTokenException() {
        super();
    }

    public MissingTokenException(Throwable cause) {
        super(cause);
    }

    public MissingTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    protected MissingTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
