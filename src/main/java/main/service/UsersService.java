package main.service;

import lombok.AllArgsConstructor;
import main.api.request.RegisterRq;
import main.api.response.ComplexRs;
import main.api.response.RegisterRs;
import main.config.entities.Captcha;
import main.config.entities.Person;
import main.repository.CaptchaRepository;
import main.repository.PersonRepository;
import main.repository.PersonsRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {
    private final PersonsRepository personsRepository;
    private final CaptchaRepository captchaRepository;


}
