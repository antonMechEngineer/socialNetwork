package main.service;

import lombok.RequiredArgsConstructor;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonCacheService {
    private final PersonsRepository personsRepository;

    @CachePut(value="persons",key = "#person.email")
    public Person cachePerson(Person person){
        personsRepository.save(person);
        return person;
    }
    public Person getPersonById(long personId) {
        String email = personsRepository.findEmailById(personId);
        return getPersonByEmail(email);
    }

    @CachePut(value = "persons")
    public Person getPersonByEmail(String email) {
        return personsRepository.findPersonByEmail(email).orElse(null);
    }

    public Person getPersonByContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getPersonByEmail(email);
    }
}
