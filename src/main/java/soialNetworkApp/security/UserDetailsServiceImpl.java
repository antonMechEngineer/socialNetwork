package soialNetworkApp.security;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.security.core.userdetails.User;
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
//        return new UserDetailsImpl(person);
        return User.builder()
                .username(person.getEmail())
                .password(person.getPassword())
                .roles("BUTLER")
                .build();
    }
}
