package gusil.mybox.exception;

public class DirectoryNotFoundException extends RuntimeException{
    public DirectoryNotFoundException(String id) {
        super("Directory " + id + " is not found");
    }
}
