package fr.zhj2074.backoffice.exception;

public class TechnicalException extends RuntimeException {

    public TechnicalException(Throwable cause) {
        super(cause);
    }

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
