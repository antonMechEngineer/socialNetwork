package socialnet.errors;

import java.util.function.Supplier;

public class NoSuchEntityException extends Exception implements Supplier<Exception> {

    public NoSuchEntityException(String message) {
        super(message);
    }

    @Override
    public Exception get() {
        return this;
    }
}
