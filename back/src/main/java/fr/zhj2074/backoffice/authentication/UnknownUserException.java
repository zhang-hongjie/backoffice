package fr.zhj2074.backoffice.authentication;

import fr.zhj2074.backoffice.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized: unknown user in ldap")
public class UnknownUserException extends AppException {
    public UnknownUserException(String msg) {
        super(msg);
    }

    public UnknownUserException() {
        super();
    }

    public UnknownUserException(Throwable cause) {
        super(cause);
    }

    public UnknownUserException(String message, Throwable cause) {
        super(message, cause);
    }

    protected UnknownUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
