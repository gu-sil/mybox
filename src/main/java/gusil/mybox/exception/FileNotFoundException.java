package gusil.mybox.exception;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(String id) {
        super("File " + id + " is not found");
    }
}
