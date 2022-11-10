package main.repository;

import main.model.entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private List<User> users;

    public UserRepository() {
        User u1 = new User();
        u1.setEmail("1@m.c");
        u1.setPassword("123");
        User u2 = new User();
        u2.setEmail("2@m.c");
        u2.setPassword("456");
        this.users = List.of(u1, u2);
    }

    public User findByEmail(String email) {
        return this.users.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public List<User> findAll() {
        return this.users;
    }
}
