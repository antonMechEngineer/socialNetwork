package main.errors;

import java.util.function.Supplier;

public class PersonNotFoundException extends Exception implements Supplier<Exception> {

    public PersonNotFoundException(String message) {
        super(message);
    }

    @Override
    public Exception get() {
        return this;
    }
}
