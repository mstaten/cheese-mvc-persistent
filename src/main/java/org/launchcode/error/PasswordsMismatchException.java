package org.launchcode.error;

public class PasswordsMismatchException extends Exception {

    public PasswordsMismatchException() {}

    public PasswordsMismatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PasswordsMismatchException(final String message) {
        super(message);
    }

    public PasswordsMismatchException(final Throwable cause) {
        super(cause);
    }
}
