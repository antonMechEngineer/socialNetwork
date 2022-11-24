package main.service;

import lombok.AllArgsConstructor;

import main.api.request.UserRq;
import main.api.response.PersonResponse;
import main.api.response.StorageDataRs;
import main.api.response.StorageRs;
import main.api.response.UserRs;
import main.mappers.PersonMapper;
import main.model.entities.Person;
import main.repository.CaptchaRepository;
import main.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Service
@AllArgsConstructor
public class UsersService {
    private final static int MAX_IMAGE_LENTH = 512000;
    private final PersonsRepository personsRepository;
    private final CaptchaRepository captchaRepository;
    private final CloudaryService cloudaryService;
    private final PersonMapper personMapper;


    public StorageRs storeImage(MultipartFile photo) throws IOException {
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        long personId = person.getId();
        StorageRs response = new StorageRs();
        StorageDataRs dataRs = new StorageDataRs();
        PersonResponse personResponse = personMapper.toPersonResponse(person);

        String originFileName = photo.getOriginalFilename();
        String extention = originFileName.substring(originFileName.lastIndexOf(".") + 1);
        String newFileName = "image" + personId + "." + extention;
        if (!extention.equals("jpg") && !extention.equals("png")) {
            response.setError("Отправлен файл не формата изображение jpg, png");
        }
        long fileLenth = photo.getSize();
        if (fileLenth > MAX_IMAGE_LENTH) {
            response.setError("Размер файла превышает допустимый размер");
        }

      //  if (response.getError().isEmpty()) {

        response.setTimestamp(0);
        dataRs.setBytes(0);
        dataRs.setCreatedAt(0);

        BufferedImage bufferedImage = ImageIO.read(photo.getInputStream());
        File outputfile = new File(newFileName);
        ImageIO.write(bufferedImage, extention, outputfile);

        String cloudinaryPath = person.getPhoto();
        if (!cloudinaryPath.equals("https://res.cloudinary.com/dre3qhjvh/image/upload/v1669013824/default-1_wzqelg.png")) {
            cloudaryService.deleteImage("image" + personId);
        }
        cloudaryService.uploadImage(outputfile);
        String relativePath = cloudaryService.getImage(newFileName);

        dataRs.setId(String.valueOf(personId));
        dataRs.setOwnerId(personId);
        dataRs.setFileName(newFileName);
        dataRs.setRelativeFilePath(relativePath);
        dataRs.setFileFormat(photo.getContentType());
        dataRs.setFileType(extention);

        response.setData(dataRs);
    //}
        return response;
    }

    public UserRs editProfile(UserRq userRq){
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        UserRs response =new UserRs();
        PersonResponse personResponse = personMapper.toPersonResponse(person);
        if (userRq.getAbout() != null) {
            person.setAbout(userRq.getAbout());
        }
        if (userRq.getBirth_date() != null) {
            person.setBirthDate(LocalDateTime.from(OffsetDateTime.parse(userRq.getBirth_date())));
            personResponse.setBirthDate(LocalDateTime.from(OffsetDateTime.parse(userRq.getBirth_date())));
        }
        if (userRq.getCity() != null) {
       //     person.setCity(userRq.getCity());
        }
        if (userRq.getCountry() != null) {
       //     person.setCountry(userRq.getCountry());
        }
        if (userRq.getFirst_name() != null) {
            person.setFirstName(userRq.getFirst_name());
            personResponse.setFirstName(userRq.getFirst_name());
        }
        if (userRq.getLast_name() != null) {
            person.setLastName(userRq.getLast_name());
            personResponse.setLastName(userRq.getLast_name());
        }
        if (userRq.getPhone() != null) {
            person.setPhone(userRq.getPhone());
            personResponse.setPhone(userRq.getPhone());
        }
        if (userRq.getPhoto_id() != null) {
            person.setPhoto(userRq.getPhoto_id());
            personResponse.setPhoto(userRq.getPhoto_id());
        }

        personsRepository.save(person);
        response.setData(personResponse);
        return response;
    }
}
