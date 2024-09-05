package cz.martinkostelecky.exampleservice.exception;

public class ExampleAlreadyExistException extends Exception {

    public ExampleAlreadyExistException(String message) {
        super(message);
    }
}
