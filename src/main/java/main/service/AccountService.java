package main.service;

import lombok.AllArgsConstructor;
import main.api.request.RegisterRq;
import main.api.response.ComplexRs;
import main.api.response.RegisterRs;
import main.config.entities.Captcha;
import main.config.entities.Person;
import main.repository.CaptchaRepository;
import main.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private final PersonRepository personRepository;
    private final CaptchaRepository captchaRepository;

    public RegisterRs getRegResponse(RegisterRq regRequest){
        RegisterRs registerRs = new RegisterRs();
        ComplexRs data = new ComplexRs();
        data.setMessage("OK");

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
        } else {
            registerRs.setError("Каптча");
            registerRs.setError_description("код устарел");
        }

        registerRs.setEmail(regRequest.getEmail());
        registerRs.setData(data);

        if (registerRs.getError().isEmpty()) {
            Person person = new Person();
            person.setFirstName(regRequest.getFirstName());
            person.setLastName(regRequest.getLastName());
            person.setPassword(regRequest.getPasswd1());
            person.setRegDate((Timestamp) new Date());
            personRepository.save(person);
        }

        return registerRs;
    }
}
