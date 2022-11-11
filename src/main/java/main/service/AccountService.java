package main.service;

import lombok.AllArgsConstructor;
import main.api.request.RegisterRq;
import main.api.response.ComplexRs;
import main.api.response.RegisterRs;
import main.config.entities.Person;
import main.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class AccountService {
    private final PersonRepository personRepository;

    public RegisterRs getRegResponse(RegisterRq regRequest){
        RegisterRs registerRs = new RegisterRs();
        ComplexRs data = new ComplexRs();
        data.setMessage("OK");

        if (!regRequest.getPasswd1().equals(regRequest.getPasswd2())){
            registerRs.setError("Ошибка в пароле!");
            registerRs.setError_description("Введены разные пароли");
            data.setMessage("404");
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
