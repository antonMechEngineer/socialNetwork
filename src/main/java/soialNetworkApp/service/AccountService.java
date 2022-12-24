package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.*;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.PersonSettingsRs;
import soialNetworkApp.api.response.RegisterRs;
import soialNetworkApp.errors.CaptchaException;
import soialNetworkApp.errors.IncorrectRequestTypeException;
import soialNetworkApp.errors.PasswordException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.model.entities.Captcha;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.PersonSettings;
import soialNetworkApp.model.enums.MessagePermissionTypes;
import soialNetworkApp.model.enums.NotificationTypes;
import soialNetworkApp.repository.CaptchaRepository;
import soialNetworkApp.repository.PersonSettingsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PersonsRepository personsRepository;
    private final CaptchaRepository captchaRepository;
    private final PersonSettingsRepository personSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService eMailService;
    private final PersonsService personsService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Value("${auth.pass-restore}")
    String basePassUrl;
    @Value("${auth.email-restore}")
    String baseEmailUrl;
    @Value("${socialNetwork.timezone}")
    private String timezone;

    public RegisterRs getRegResponse(RegisterRq regRequest) throws PasswordException, CaptchaException {
        RegisterRs registerRs = new RegisterRs();
        ComplexRs data = ComplexRs.builder().message("OK").build();

        if (!regRequest.getPasswd1().equals(regRequest.getPasswd2())) {
            throw new PasswordException("Different passwords entered");
        }
        String captcha = regRequest.getCode();
        String secret = regRequest.getCodeSecret();
        Optional<Captcha> optionalCaptcha = captchaRepository.findCaptchaBySecretCode(secret);
        if (optionalCaptcha.isPresent()) {
            if (!optionalCaptcha.get().getCode().equals(captcha)) {
                throw new CaptchaException("Invalid captcha entered");
            }
        }
        registerRs.setEmail(regRequest.getEmail());
        registerRs.setData(data);
        Person person = new Person();
        person.setFirstName(regRequest.getFirstName());
        person.setLastName(regRequest.getLastName());
        person.setPassword(passwordEncoder.encode(regRequest.getPasswd1()));
        person.setRegDate(LocalDateTime.now(ZoneId.of(timezone)));
        person.setEmail(regRequest.getEmail());
        person.setPhoto("https://res.cloudinary.com/dre3qhjvh/image/upload/v1669013824/default-1_wzqelg.png");
        person.setIsApproved(false);
        person.setIsBlocked(false);
        person.setIsDeleted(false);
        person.setEmail(regRequest.getEmail());
        person.setMessagePermission(MessagePermissionTypes.ALL);
        person.setPersonSettings(createDefaultNotificationsSettings(person));
        personsRepository.save(person);
        return registerRs;
    }

    public RegisterRs getPasswordSet(PasswordSetRq passwordSetRq) {
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

    public RegisterRs getPasswordReSet(PasswordRq passwordRq) {
        Optional<Person> optPerson = personsRepository.checkToken(passwordRq.getSecret());
        Person rescuePerson = null;
        if (optPerson.isPresent()) {
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

    public RegisterRs getPasswordRecovery(String email) {
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
        String text = basePassUrl + token;
        eMailService.sendSimpleMessage(to, subject, text);

        return response;
    }

    public RegisterRs getEmailRecovery() {
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
        String text = baseEmailUrl + token;
        eMailService.sendSimpleMessage(to, subject, text);
        return response;
    }

    private PersonSettings createDefaultNotificationsSettings(Person person) {
        PersonSettings settings = new PersonSettings();
        settings.setPerson(person);
        settings.setPostNotification(true);
        settings.setPostCommentNotification(true);
        settings.setCommentCommentNotification(true);
        settings.setLikeNotification(true);
        settings.setFriendBirthdayNotification(true);
        settings.setFriendRequestNotification(true);
        settings.setMessageNotification(true);
        return settings;
    }

    public CommonRs<ComplexRs> setPersonSetting(PersonSettingsRq request) throws PersonNotFoundException, IncorrectRequestTypeException {
        Person person = personsService.getPersonByContext();
        if (person == null) {
            throw new PersonNotFoundException("Person not found");
        }
        PersonSettings personSettings = person.getPersonSettings();
        switch (request.getNotificationType()) {
            case "POST": {
                personSettings.setPostNotification(request.isEnable());
                break;
            }
            case "POST_COMMENT": {
                personSettings.setPostCommentNotification(request.isEnable());
                break;
            }
            case "COMMENT_COMMENT": {
                personSettings.setCommentCommentNotification(request.isEnable());
                break;
            }
            case "FRIEND_REQUEST": {
                personSettings.setFriendRequestNotification(request.isEnable());
                break;
            }
            case "MESSAGE": {
                personSettings.setMessageNotification(request.isEnable());
                break;
            }
            case "FRIEND_BIRTHDAY": {
                personSettings.setFriendBirthdayNotification(request.isEnable());
                break;
            }
            case "POST_LIKE": {
                personSettings.setLikeNotification(request.isEnable());
                break;
            }
            default:
                throw new IncorrectRequestTypeException("Incorrect notification type");
        }
        personSettingsRepository.save(personSettings);
        return CommonRs.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(new ComplexRs())
                .build();
    }

    public CommonRs<List<PersonSettingsRs>> getPersonSettings() throws PersonNotFoundException {
        Person person = personsService.getPersonByContext();
        if (person == null) {
            throw new PersonNotFoundException("Person not found");
        }
        PersonSettings personSettings = person.getPersonSettings();
        if (personSettings == null) {
            person.setPersonSettings(createDefaultNotificationsSettings(person));
            personSettings = personsRepository.save(person).getPersonSettings();
        }
        PersonSettingsRs postValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.POST)).enable(personSettings.getPostNotification()).build();
        PersonSettingsRs postCommentValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.POST_COMMENT)).enable(personSettings.getPostCommentNotification()).build();
        PersonSettingsRs commentCommentValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.COMMENT_COMMENT)).enable(personSettings.getCommentCommentNotification()).build();
        PersonSettingsRs friendRequestValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.FRIEND_REQUEST)).enable(personSettings.getFriendRequestNotification()).build();
        PersonSettingsRs messageValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.MESSAGE)).enable(personSettings.getMessageNotification()).build();
        PersonSettingsRs friendsBirthdayValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.FRIEND_BIRTHDAY)).enable(personSettings.getFriendBirthdayNotification()).build();
        PersonSettingsRs postLikeValue = PersonSettingsRs.builder()
                .type(String.valueOf(NotificationTypes.POST_LIKE)).enable(personSettings.getLikeNotification()).build();
        return CommonRs.<List<PersonSettingsRs>>builder()
                .timestamp(System.currentTimeMillis())
                .data(List.of(
                        postValue,
                        postCommentValue,
                        commentCommentValue,
                        friendRequestValue,
                        messageValue,
                        friendsBirthdayValue,
                        postLikeValue))
                .build();
    }
}
