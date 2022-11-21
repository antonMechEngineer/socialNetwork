package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.PersonResponse;
import main.model.entities.Person;
import main.security.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonsService personsService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public CommonResponse<PersonResponse> loginUser(LoginRq loginRq) {
        Person person = personsService.getPersonByEmail(loginRq.getEmail());
        if (person != null) {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRq.getEmail(), loginRq.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                PersonResponse response = personsService.getPersonResponse(personsService.getPersonByEmail(loginRq.getEmail()));
                String token = jwtUtil.createToken(person.getEmail());
                response.setToken(token);
                return CommonResponse.<PersonResponse>builder()
                        .error("success")
                        .errorDescription("")
                        .offset(0)
                        .perPage(0)
                        .timestamp(System.currentTimeMillis())
                        .data(response)
                        .build();
            }
        }
        return null;
    }

    public CommonResponse<ComplexRs> logoutUser() {
        SecurityContextHolder.clearContext();
        return CommonResponse.<ComplexRs>builder()
                .error("logout")
                .timestamp(System.currentTimeMillis())
                .offset(0)
                .perPage(0)
                .errorDescription("")
                .data(new ComplexRs())
                .build();
    }
}
