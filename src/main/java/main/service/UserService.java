package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.LoginRs;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JWTUtil jwtUtil;
    private final PersonsRepository personsRepository;

    public LoginRs login(LoginRq login) {
        if (personsRepository.existsPersonByEmail(login.getEmail())) {
            return LoginRs.builder().result(jwtUtil.createToken(login.getEmail())).build();
        }
        return null;
    }
}
