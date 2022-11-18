package main.service;

import main.api.response.PersonResponse;
import main.errors.PersonNotFoundByEmailException;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsRecommendationService {

    private final PersonsRepository personsRepository;

    @Autowired
    public FriendsRecommendationService(PersonsRepository personsRepository) {
        this.personsRepository = personsRepository;
    }

    public List<PersonResponse> getFriendsRecommendation(Principal principal) throws PersonNotFoundByEmailException {
        Optional<Person> optionalPerson = personsRepository.findPersonByEmail(principal.getName());
        Person person;
        Pageable page = PageRequest.of(0, 8);
        if (optionalPerson.isPresent()) {
            person = optionalPerson.get();
        } else {
            throw new PersonNotFoundByEmailException("Person with email - " + principal.getName() + " not found");
        }
        List<PersonResponse> personResponses = personsToPersonResponses(personsRepository.findAllByCity(person.getCity(), page).getContent());
        if (person.getCity() == null || personResponses.size() == 0) {
            return personsToPersonResponses(personsRepository.findPageOrderByRegDate(page).getContent());
        }
        return personResponses;
    }

    private List<PersonResponse> personsToPersonResponses(List<Person> persons) {
        List<PersonResponse> personResponses = new ArrayList<>();
        for (Person person : persons) {
            personResponses.add(PersonMapper.INSTANCE.toPersonResponse(person));
        }
        return personResponses;
    }
}
