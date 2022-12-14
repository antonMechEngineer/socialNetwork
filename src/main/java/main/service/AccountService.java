package main.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import main.api.request.EmailRq;
import main.api.request.PasswordRq;
import main.api.request.PasswordSetRq;
import main.api.request.RegisterRq;
import main.api.response.ComplexRs;
import main.api.response.RegisterRs;
import main.model.entities.Captcha;
import main.model.entities.Person;
import main.model.enums.MessagePermissionTypes;
import main.repository.CaptchaRepository;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PersonsRepository personsRepository;
    private final CaptchaRepository captchaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EMailService eMailService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Value("${auth.pass-restore}")
    String basePassUrl;
    @Value("${auth.email-restore}")
    String baseEmailUrl;

    public RegisterRs getRegResponse(RegisterRq regRequest){
        RegisterRs registerRs = new RegisterRs();
        ComplexRs data = ComplexRs.builder().message("OK").build();

        if (!regRequest.getPasswd1().equals(regRequest.getPasswd2())){
            registerRs.setError("Ошибка в пароле!");
            registerRs.setError_description("Введены разные пароли");
            data.setMessage("404");
        }
        String captcha = regRequest.getCode();
        String secret = regRequest.getCodeSecret();
        Optional<Captcha> optionalCaptcha = captchaRepository.findCaptchaBySecretCode(secret);
        if (optionalCaptcha.isPresent()) {
            if (!optionalCaptcha.get().getCode().equals(captcha)) {
                registerRs.setError("Каптча");
                registerRs.setError_description("Код с картинки введён неверно");
            }
        }

        registerRs.setEmail(regRequest.getEmail());
        registerRs.setData(data);
        if (registerRs.getError()==null) {
            Person person = new Person();
            person.setFirstName(regRequest.getFirstName());
            person.setLastName(regRequest.getLastName());
            person.setPassword(passwordEncoder.encode(regRequest.getPasswd1()));
            person.setRegDate(LocalDateTime.now());
            person.setEmail(regRequest.getEmail());
            person.setPhoto("https://res.cloudinary.com/dre3qhjvh/image/upload/v1669013824/default-1_wzqelg.png");
            person.setIsApproved(false);
            person.setIsBlocked(false);
            person.setIsDeleted(false);
            person.setEmail(regRequest.getEmail());
            person.setMessagePermission(MessagePermissionTypes.ALL);
            personsRepository.save(person);
        }
        return registerRs;
    }

    public RegisterRs getPasswordSet(PasswordSetRq passwordSetRq){
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder
                .getContext().getAuthentication().getName()).get();
        RegisterRs response = new RegisterRs();
        ComplexRs data = ComplexRs.builder()
                .id(0)
                .count(0)
                .message("OK")
                .message_id(0L)
                .build();
        response.setEmail(person.getEmail());
        response.setTimestamp(0);
        response.setData(data);

        person.setPassword(passwordEncoder.encode(passwordSetRq.getPassword()));
        personsRepository.save(person);

        return response;
    }

    public RegisterRs getPasswordReSet(PasswordRq passwordRq){
        Optional<Person> optPerson = personsRepository.checkToken(passwordRq.getSecret());
        Person rescuePerson = null;
        if (optPerson.isPresent()){
            rescuePerson = optPerson.get();
        }

        RegisterRs response = new RegisterRs();
        ComplexRs data = ComplexRs.builder()
                .id(0)
                .count(0)
                .message("OK")
                .message_id(0L)
                .build();
        response.setEmail(rescuePerson.getEmail());
        response.setTimestamp(0);
        response.setData(data);

        rescuePerson.setPassword(passwordEncoder.encode(passwordRq.getPassword()));
        personsRepository.save(rescuePerson);

        return response;
    }
    public RegisterRs getPasswordRecovery(String email){
        RegisterRs response = new RegisterRs();
        ComplexRs data = ComplexRs.builder()
                .id(0)
                .count(0)
                .message("OK")
                .message_id(0L)
                .build();
        response.setEmail(email);
        response.setTimestamp(0);
        response.setData(data);

        Person person = personsRepository.findPersonByEmail(email).get();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Person userToRestore = person;
        userToRestore.setChangePasswordToken(token);
        personsRepository.save(userToRestore);

        String to = email;
        String subject = "Восстановление пароля";
        String text =  basePassUrl+ token;
        eMailService.sendSimpleMessage(to, subject, text);

        return response;
    }

    public RegisterRs getEmailRecovery(){
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder
                .getContext().getAuthentication().getName()).get();
        RegisterRs response = new RegisterRs();
        ComplexRs data = ComplexRs.builder()
                .id(0)
                .count(0)
                .message("OK")
                .message_id(0L)
                .build();
        response.setEmail(person.getEmail());
        response.setTimestamp(0);
        response.setData(data);

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        person.setChangePasswordToken(token);
        personsRepository.save(person);

        String to = person.getEmail();
        String subject = "Смена ящика почты";
        String text =  baseEmailUrl+ token;
        eMailService.sendSimpleMessage(to, subject, text);
        return response;
    }
}
