package ua.hudyma.exception;

public class EntityNotDeletedException extends RuntimeException {
    public EntityNotDeletedException(String message) {
        super(message);
    }
}
