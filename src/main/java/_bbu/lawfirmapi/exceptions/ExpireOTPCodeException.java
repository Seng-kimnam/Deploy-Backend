package _bbu.lawfirmapi.exceptions;

public class ExpireOTPCodeException extends RuntimeException {
    public ExpireOTPCodeException(String message) {
        super(message);
    }
}
