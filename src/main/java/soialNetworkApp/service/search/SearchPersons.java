package soialNetworkApp.service.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import soialNetworkApp.api.request.FindPersonRq;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static soialNetworkApp.service.search.PersonSpecification.*;

@Component
@RequiredArgsConstructor
public class SearchPersons {

    private final CommonSearchMethods commonSearchMethods;
    private final PersonsRepository personsRepository;

    public Page<Person> findPersons(FindPersonRq personRq, int offset, int perPage) {
        Person me = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Pageable pageable = PageRequest.of(offset, perPage);
        Specification<Person> specification = excludeMe(me.getId());
        if (personRq.getFirst_name() != null && personRq.getLast_name() == null && personRq.getCity() == null &&
                personRq.getCountry() == null && personRq.getAge_to() == null && personRq.getAge_from() == null) {
            List<Person> persons = commonSearchMethods.findPersonByNameAndLastNameContains(personRq.getFirst_name());
            List<Long> personsIds = new ArrayList<>();
            persons.forEach(person -> personsIds.add(person.getId()));
            specification = specification.and(onlyNameInRequest(personsIds));
            return personsRepository.findAll(specification, pageable);
        }
        if (personRq.getFirst_name() != null) {
            specification = specification.and(nameContains(personRq.getFirst_name()));
        }
        if (personRq.getLast_name() != null) {
            specification = specification.and(lastNameContains(personRq.getLast_name()));
        }
        if (personRq.getAge_from() != null) {
            specification = specification.and(ageFrom(personRq.getAge_from()));
        }
        if (personRq.getAge_to() != null) {
            specification = specification.and(ageTo(personRq.getAge_to()));
        }
        if (personRq.getCountry() != null) {
            specification = specification.and(personCountry(personRq.getCountry()));
        }
        if (personRq.getCity() != null) {
            specification = specification.and(personCity(personRq.getCity()));
        }
        return personsRepository.findAll(specification, pageable);
    }
}
