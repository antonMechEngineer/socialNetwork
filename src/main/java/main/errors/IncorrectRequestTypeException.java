package main.errors;

public class IncorrectRequestTypeException extends Exception{

    public IncorrectRequestTypeException(String message) {
        super(message);
    }
}
