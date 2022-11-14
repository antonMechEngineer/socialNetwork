package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.LoginRs;
import main.repository.UserRepository;
import main.security.UserDetailsImpl;
import main.security.UserDetailsServiceImpl;
import main.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JWTUtil jwtUtil;


    public LoginRs login(LoginRq login) {
        return LoginRs.builder().result(jwtUtil.createToken(login.getEmail())).build();
    }

}
