package gusil.mybox.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String userId) {
        super(userId);
    }
}
