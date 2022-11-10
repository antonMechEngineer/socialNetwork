package main.errors;

public class NoPostEntityException extends Exception {
    public NoPostEntityException(String message) {
        super(message);
    }
}
