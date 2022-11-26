package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.PersonsRepository;
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
//        List<PersonResponse> personResponses = personsToPersonResponses(personsRepository.findAllByCity(person.getCity(), page).getContent());
        List<PersonResponse> personResponses = personsToPersonResponses(personsRepository.findAllByCity(person.getCity(), page).getContent());
        if (person.getCity() == null || personResponses.size() == 0) {
            personResponses = personsToPersonResponses(personsRepository.findPageOrderByRegDate(page).getContent());
            return buildCommonResponse(personResponses);
        }
        return buildCommonResponse(personResponses);
    }

    private CommonResponse<List<PersonResponse>> buildCommonResponse(List<PersonResponse> personResponses) {
        return CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .total((long) personResponses.size())
                .data(personResponses)
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
