package socialnet.errors;

import socialnet.model.entities.Person;

import java.util.function.Supplier;

public class FriendshipNotFoundException extends Exception implements Supplier<Exception> {

    public FriendshipNotFoundException(Person firstFriend, Person secondFriend) {
        super("Friendship is not existing with first friend = " + firstFriend.toString()
                + " and second friend = " + secondFriend.toString());
    }

    @Override
    public Exception get() {
        return this;
    }
}
