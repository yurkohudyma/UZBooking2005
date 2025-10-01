package ua.hudyma.exception;

public class TicketOverbookingException extends RuntimeException {

    public TicketOverbookingException(String s) {
        super(s);
    }
}
