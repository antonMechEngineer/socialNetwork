package socialnet.service.search;

import lombok.RequiredArgsConstructor;
import socialnet.model.entities.Person;
import socialnet.repository.PersonsRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonSearchMethods {
    private final PersonsRepository personsRepository;

    public LocalDateTime longToLocalDateTime(Long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public List<Person> findPersonByNameAndLastNameContains(String name) {
        List<Person> persons = null;
        String[] splitName = name.split("\\s+");
        if (splitName.length < 2) {
            persons = personsRepository.findPersonByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(name, name);
        }
        if (splitName.length > 1) {
            persons = personsRepository.findPersonByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(splitName[0], splitName[1]);
        }
        assert persons != null;
        if (persons.size() == 0 && splitName.length > 1) {
            persons = personsRepository.findPersonByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(splitName[1], splitName[0]);
        }
        return persons;
    }

}
