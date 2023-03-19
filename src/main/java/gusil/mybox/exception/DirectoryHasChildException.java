package gusil.mybox.exception;

public class DirectoryHasChildException extends RuntimeException {
    public DirectoryHasChildException(String id) {
        super("Directory " + id + " has child");
    }
}
