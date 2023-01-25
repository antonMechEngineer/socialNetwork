package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.request.LoginRq;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.errors.PasswordException;
import soialNetworkApp.errors.WrongEmailException;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.security.jwt.JWTUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonsRepository personsRepository;
    private final PersonCacheService personCacheService;
    private final PasswordEncoder passwordEncoder;
    private final PersonMapper personMapper;
    private final JWTUtil jwtUtil;

    public CommonRs<PersonRs> loginUser(LoginRq loginRq, Long telegramId) throws PasswordException, WrongEmailException {
        Person person = personCacheService.getPersonByEmail(loginRq.getEmail());
        if (person != null) {
            if (!passwordEncoder.matches(loginRq.getPassword(), person.getPassword())) {
                throw new PasswordException("Wrong password for email: '" + loginRq.getEmail() + "'");
            } else {
                if (telegramId != null) {
                    person.setTelegramId(telegramId);
                    personsRepository.save(person);
                }
                return buildCommonResponse(person);
            }
        } else {
            throw new WrongEmailException("User with email: '" + loginRq.getEmail() + "' not found");
        }
    }

    public CommonRs<ComplexRs> logoutUser() {
        SecurityContextHolder.clearContext();
        return CommonRs.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    private CommonRs<PersonRs> buildCommonResponse(Person person) {
        PersonRs user = personMapper.toPersonResponse(person);
        user.setToken(jwtUtil.createToken(person.getEmail()));
        return CommonRs.<PersonRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(user)
                .build();
    }
}
