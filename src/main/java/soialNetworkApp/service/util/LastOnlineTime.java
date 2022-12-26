package soialNetworkApp.service.util;

import lombok.AllArgsConstructor;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;

import java.time.LocalDateTime;

@AllArgsConstructor
public class LastOnlineTime {

    public static void saveLastOnlineTime(Person person, PersonsRepository personsRepository) {
        person.setLastOnlineTime(LocalDateTime.now());
        personsRepository.save(person);
    }
}
