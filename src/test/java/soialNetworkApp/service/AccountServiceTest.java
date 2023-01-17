package soialNetworkApp.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import soialNetworkApp.model.entities.Captcha;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.CaptchaRepository;
import soialNetworkApp.repository.PersonsRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class AccountServiceTest {

    @MockBean
    private CaptchaRepository captchaRepository;

    @MockBean
    private PersonsRepository personsRepository;

    @Test
    void testRegResponse(){
        Captcha captcha = new Captcha();
        when(captchaRepository.findCaptchaBySecretCode(any())).thenReturn(Optional.of(captcha));
    }
    @Test
    void testPasswordReSet(){
        Person person = new Person();
        when(personsRepository.checkToken(any())).thenReturn(Optional.of(person));

    }
}
