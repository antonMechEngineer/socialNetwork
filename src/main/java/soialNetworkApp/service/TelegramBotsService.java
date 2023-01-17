package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;

import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class TelegramBotsService {

    private final PersonsRepository personsRepository;

    public String getUserInfo(String email) {
        Person person = personsRepository.findPersonByEmail(email).orElse(null);
        if (person == null) {
            return "Person not found!";
        }
        StringJoiner response = new StringJoiner(System.lineSeparator());
        response.add("First name " + person.getFirstName())
                .add("Last name " + person.getLastName())
                .add("Birthday " + person.getBirthDate().toString())
                .add("Country " + person.getCountry())
                .add("City " + person.getCity())
                .add("Count of posts " + person.getPosts().size())
                .add("Count of comments " + person.getComments().size())
                .add("Count of likes " + person.getLikes().size());
        return response.toString();
    }
}
