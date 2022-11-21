package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PersonsService {

    private final PersonsRepository personRepository;
    private final CurrencyService currencyService;

    public Person getPersonById(long personId) {
        Logger.getLogger(this.getClass().getName()).info("getPersonById with id " + personId);
        return personRepository.findById(personId).orElse(null);
    }

    public Person getPersonByEmail(String email) {
        Logger.getLogger(this.getClass().getName()).info("getPersonByEmail with email " + email);
        return personRepository.findPersonByEmail(email).orElse(null);
    }

    public UserRs editImage(Principal principal, MultipartFile photo, String phone, String about,
                            String city, String country, String first_name, String last_name,
                            String birth_date, String message_permission) throws IOException {
        Person person = personRepository.findPersonByEmail(principal.getName()).get();
        UserRs response =new UserRs();

        return response;
    }

    public PersonResponse getPersonResponse(Person person) {
        return PersonMapper.INSTANCE.toPersonResponse(person);
    }
}
