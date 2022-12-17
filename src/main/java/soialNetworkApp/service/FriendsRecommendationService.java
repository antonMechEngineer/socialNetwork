package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsRecommendationService {

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;

    public CommonResponse<List<PersonResponse>> getFriendsRecommendation() {
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Pageable page = PageRequest.of(0, 8);
        Page<Person> persons = personsRepository.findAllByCity(person.getCity(), page);
        if (person.getCity() == null || persons.getTotalElements() == 0) {
            persons = personsRepository.findPageOrderByRegDate(page);
            return buildCommonResponse(persons, 0, 8);
        }
        return buildCommonResponse(persons, 0, 8);
    }

    private CommonResponse<List<PersonResponse>> buildCommonResponse(Page<Person> persons, int offset, int perPage) {
        return CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .total(persons.getTotalElements())
                .offset(offset)
                .perPage(perPage)
                .data(personsToPersonResponses(persons.getContent()))
                .build();
    }

    private List<PersonResponse> personsToPersonResponses(List<Person> persons) {
        List<PersonResponse> personResponses = new ArrayList<>();
        for (Person person : persons) {
            personResponses.add(personMapper.toPersonResponse(person));
        }
        return personResponses;
    }
}
