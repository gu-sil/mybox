package gusil.mybox.exception;

public class UserUsageExceedsException extends RuntimeException {
    public UserUsageExceedsException(String userId) {
        super(userId + " usage exceeds");
    }
}
