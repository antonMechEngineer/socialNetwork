package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CurrencyRateRs;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.api.response.WeatherRs;
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
        return PersonResponse.builder()
                .id(person.getId())
                .email(person.getEmail())
                .phone(person.getPhone())
                .photo(person.getPhoto())
                .about(person.getAbout())
                .city(person.getCity().getTitle())
                .country(person.getCity().getCountry().getTitle())
                .token(person.getChangePasswordToken())
                .weather(WeatherRs.builder()
                        .temp(person.getCity().getTemp())
                        .clouds(person.getCity().getClouds())
                        .build())
                .currency(CurrencyRateRs.builder()
                        .euro(currencyService.getCurrencyByName("EUR").getPrice())
                        .usd(currencyService.getCurrencyByName("USD").getPrice())
                        .build())
                .online(Boolean.valueOf(person.getOnlineStatus()))
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .regDate(person.getRegDate())
                .birthDate(person.getBirthDate())
                .messagePermission(person.getMessagePermission())
                .lastOnlineTime(person.getLastOnlineTime())
                .isBlocked(person.getIsBlocked())
                .isDeleted(person.getIsDeleted())
                .build();
    }
}
