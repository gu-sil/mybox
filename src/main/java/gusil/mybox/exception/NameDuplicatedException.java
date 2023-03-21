package gusil.mybox.exception;

public class NameDuplicatedException extends RuntimeException {
    public NameDuplicatedException(String name) {
        super(name + " is duplicated");
    }
}
