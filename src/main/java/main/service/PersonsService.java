package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.errors.BadAuthorizationException;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PersonsService {

    private final PersonsRepository personRepository;
    private final CurrencyService currencyService;
    private final JWTUtil jwtUtil;
    private final PersonMapper personMapper;

    public CommonResponse<PersonResponse> getAuthorized(String token) throws BadAuthorizationException {
        if (jwtUtil.isValidToken(token)) {
            throw new BadAuthorizationException("Invalid token");
        }
        return CommonResponse.<PersonResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(personMapper.toPersonResponse(getPersonByEmail(jwtUtil.extractUserName(token))))
                .build();
    }

    public Person getPersonById(long personId) {
        return personRepository.findById(personId).orElse(null);
    }

    public Person getPersonByEmail(String email) {
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
        return personMapper.toPersonResponse(person);
    }
}
