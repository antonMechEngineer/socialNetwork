package main.errors;

public class BadAuthorizationException extends Exception{

    public BadAuthorizationException(String message) {
        super(message);
    }
}
