package main.service;

import main.api.response.UserRs;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
}
