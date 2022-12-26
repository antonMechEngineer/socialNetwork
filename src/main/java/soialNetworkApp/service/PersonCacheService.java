package soialNetworkApp.service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;

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
        return getPersonByEmail(personsRepository.findPersonById(personId).get().getEmail());
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

