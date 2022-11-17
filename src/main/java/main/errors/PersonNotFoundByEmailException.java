package main.errors;

public class PersonNotFoundByEmailException extends Exception {

    public PersonNotFoundByEmailException(String message) {
        super(message);
    }
}
