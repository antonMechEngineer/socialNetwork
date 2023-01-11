package soialNetworkApp.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;

@Component
@RequiredArgsConstructor
public class CurrentUserExtractor {
    private final PersonsRepository personsRepository;

    public Person getPerson() {
        return personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }
}
