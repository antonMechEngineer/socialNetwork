package main.service;

import main.model.entities.Person;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatesService {

    public boolean validatePassword(Person person, String password) {
        return person.getPassword().equals(password);
    }
}
