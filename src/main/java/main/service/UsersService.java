package main.service;

import lombok.AllArgsConstructor;
import main.repository.CaptchaRepository;
import main.repository.PersonsRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UsersService {
    private final PersonsRepository personsRepository;
    private final CaptchaRepository captchaRepository;


}
