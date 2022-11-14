package main.service;

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
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public LoginRs login(LoginRq login) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        LoginRs loginRs = new LoginRs();
        loginRs.setResult("true");
        return loginRs;
    }

    public LoginRs jwtLogin(LoginRq login) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(login.getEmail());
        String jwtToken = jwtUtil.generateToken(userDetails);
        LoginRs loginRs = new LoginRs();
        loginRs.setResult(jwtToken);
        return loginRs;
    }
}
