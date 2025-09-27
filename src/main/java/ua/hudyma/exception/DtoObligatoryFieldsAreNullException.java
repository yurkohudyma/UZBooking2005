package ua.hudyma.exception;

public class DtoObligatoryFieldsAreNullException extends RuntimeException {
    public DtoObligatoryFieldsAreNullException(String message) {
        super(message);
    }
}
