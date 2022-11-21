package main.service;

import liquibase.util.file.FilenameUtils;
import lombok.AllArgsConstructor;

import main.api.request.UserRq;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.CaptchaRepository;
import main.repository.PersonsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Principal;


@Service
@AllArgsConstructor
public class UsersService {
    private final PersonsRepository personsRepository;
    private final CaptchaRepository captchaRepository;
    private final CloudaryService cloudaryService;

    public UserRs editImage(Principal principal, MultipartFile photo, String phone, String about,
                            String city, String country, String first_name, String last_name,
                            String birth_date, String message_permission) throws IOException {
        Person person = personsRepository.findPersonByEmail(principal.getName()).get();
        UserRs response =new UserRs();
        PersonResponse personResponse = PersonMapper.INSTANCE.toPersonResponse(person);

        String extension = (photo.getOriginalFilename());
        cloudaryService.uploadImage((File) photo);
        personResponse.setPhoto(cloudaryService.getImage(photo.getOriginalFilename()));

        return response;
    }

    public UserRs editProfile(Principal principal, UserRq userRq) throws IOException {
        Person person = personsRepository.findPersonByEmail(principal.getName()).get();
        UserRs response =new UserRs();
        PersonResponse personResponse = PersonMapper.INSTANCE.toPersonResponse(person);
        if (userRq.getAbout() != null) {
            person.setAbout(userRq.getAbout());
        }
        if (userRq.getBirth_date() != null) {
           // person.setBirthDate(userRq.getBirth_date());
        }
        if (userRq.getCity() != null) {
       //     person.setCity(userRq.getCity());
        }
        if (userRq.getCountry() != null) {
       //     person.setCountry(userRq.getCountry());
        }
        if (userRq.getFirst_name() != null) {
            person.setFirstName(userRq.getFirst_name());
        }
        if (userRq.getLast_name() != null) {
            person.setLastName(userRq.getLast_name());
        }
        if (userRq.getPhone() != null) {
            person.setPhone(userRq.getPhone());
        }



        return response;
    }


}
