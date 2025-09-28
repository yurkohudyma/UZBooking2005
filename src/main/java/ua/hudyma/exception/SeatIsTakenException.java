package ua.hudyma.exception;

public class SeatIsTakenException extends RuntimeException {
    public SeatIsTakenException(String message) {
        super(message);
    }
}
