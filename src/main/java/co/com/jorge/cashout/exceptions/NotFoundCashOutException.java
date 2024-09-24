package co.com.jorge.cashout.exceptions;

public class NotFoundCashOutException extends RuntimeException{
    public NotFoundCashOutException(String message) {
        super(message);
    }
}
