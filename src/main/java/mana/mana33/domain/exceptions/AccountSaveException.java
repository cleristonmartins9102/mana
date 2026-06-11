package mana.mana33.domain.exceptions;

public class AccountSaveException extends RuntimeException {
    public AccountSaveException(String message) {
        super(message);
    }

    public AccountSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
