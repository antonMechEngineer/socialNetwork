package main.security;

import lombok.RequiredArgsConstructor;
import main.model.entities.Person;
import main.repository.PersonsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonsRepository personsRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personsRepository.findPersonByEmail(email).orElseThrow();
        return new UserDetailsImpl(person);
    }
}
