package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PersonsService {

    private final PersonsRepository personsRepository;
    private final CurrencyService currencyService;
    private final PersonMapper personMapper;

    public CommonResponse<PersonResponse> getPersonDataById(Long id) {
        return getCommonPersonResponse(getPersonById(id));
    }

    public CommonResponse<PersonResponse> getMyData() {
        return getCommonPersonResponse(getPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public UserRs editImage(Principal principal, MultipartFile photo, String phone, String about,
                            String city, String country, String first_name, String last_name,
                            String birth_date, String message_permission) throws IOException {
        Person person = personsRepository.findPersonByEmail(principal.getName()).get();
        UserRs response = new UserRs();

        return response;
    }

    public Person getPersonById(long personId) {
        return personsRepository.findById(personId).orElse(null);
    }

    public Person getPersonByEmail(String email) {
        return personsRepository.findPersonByEmail(email).orElse(null);
    }

    public Person getPersonByContext() {
        return personsRepository.findPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElse(null);
    }

    public boolean validatePerson(Person person) {
        return person != null && person.equals(getPersonByContext());
    }

    private CommonResponse<PersonResponse> getCommonPersonResponse(Person person) {
        return CommonResponse.<PersonResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(personMapper.toPersonResponse(person))
                .build();
    }
}
