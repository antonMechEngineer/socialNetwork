package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.LoginRs;
import main.security.jwt.JWTUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JWTUtil jwtUtil;

    public LoginRs login(LoginRq login) {
        return LoginRs.builder().result(jwtUtil.createToken(login.getEmail())).build();
    }
}
