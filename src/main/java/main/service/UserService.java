package main.service;

import main.model.entities.User;
import main.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    public User getByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(String.format("User %s is not found", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, new HashSet<>());
    }
}
