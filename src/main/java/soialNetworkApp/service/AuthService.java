package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import soialNetworkApp.api.request.LoginRq;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.errors.PasswordException;
import soialNetworkApp.errors.WrongEmailException;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.security.jwt.JWTUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonsService personsService;

    private final PasswordEncoder passwordEncoder;

    private final PersonMapper personMapper;

    private final JWTUtil jwtUtil;

    public CommonResponse<PersonResponse> loginUser(LoginRq loginRq) throws PasswordException, WrongEmailException {
        Person person = personsService.getPersonByEmail(loginRq.getEmail());
        if (person != null) {
            if (!passwordEncoder.matches(loginRq.getPassword(), person.getPassword())) {
                throw new PasswordException("Wrong password for email: '" + loginRq.getEmail() + "'");
            } else {
                return buildCommonResponse(person);
            }
        } else {
            throw new WrongEmailException("User with email: '" + loginRq.getEmail() + "' not found");
        }
    }

    public CommonResponse<ComplexRs> logoutUser() {
        SecurityContextHolder.clearContext();
        return CommonResponse.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    private CommonResponse<PersonResponse> buildCommonResponse(Person person) {
        PersonResponse user = personMapper.toPersonResponse(person);
        user.setToken(jwtUtil.createToken(person.getEmail()));
        return CommonResponse.<PersonResponse>builder()
                .timestamp(System.currentTimeMillis())
                .data(user)
                .build();
    }
}
