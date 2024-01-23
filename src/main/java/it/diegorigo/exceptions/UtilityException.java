package it.diegorigo.exceptions;

public class UtilityException  extends Exception{
    public UtilityException(String message) {
        super(message);
    }

    public UtilityException(String message,
                            Throwable cause) {
        super(message, cause);
    }
}
