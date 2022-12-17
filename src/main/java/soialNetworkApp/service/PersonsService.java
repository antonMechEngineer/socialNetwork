package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonsService {

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;
    private final FriendsService friendsService;

    public CommonResponse<PersonResponse> getPersonDataById(Long id) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Person person = getPersonById(id);
        person.setFriendStatus(friendsService.getStatusTwoPersons(person, srcPerson));
        return getCommonPersonResponse(person);
    }

    public CommonResponse<PersonResponse> getMyData() {
        return getCommonPersonResponse(getPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
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
                .timestamp(System.currentTimeMillis())
                .data(personMapper.toPersonResponse(person))
                .build();
    }
}
