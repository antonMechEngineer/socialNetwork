package main.service;

import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PersonsService {

    private final PersonsRepository personRepository;

    @Autowired
    public PersonsService(PersonsRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPersonById(long personId) {
        Logger.getLogger(this.getClass().getName()).info("getPersonById with id " + personId);
        return personRepository.findById(personId).get();
    }
}
