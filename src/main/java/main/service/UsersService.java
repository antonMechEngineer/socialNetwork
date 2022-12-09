package main.service;


import lombok.RequiredArgsConstructor;
import main.api.request.FindPersonRq;
import main.api.request.UserRq;
import main.api.response.*;
import main.errors.EmptyFieldException;
import main.mappers.PersonMapper;
import main.model.entities.City;
import main.model.entities.Country;
import main.model.entities.Person;
import main.repository.*;
import main.service.search.SearchPersons;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@EnableScheduling
public class UsersService {
    private final static int MAX_IMAGE_LENTH = 512000;
    @Value("${user.time-to-delete}")
    long timeToDel;

    private final BlockHistoriesRepository blockHistoriesRepository;
    private final CommentsRepository commentsRepository;
    private final DialogsRepository dialogsRepository;
    private final FriendshipsRepository friendshipsRepository;
    private final LikesRepository likesRepository;
    private final MessagesRepository messagesRepository;
    private final NotificationsRepository notificationsRepository;
    private final PostsRepository postsRepository;
    private final PersonsRepository personsRepository;
    private final PersonSettingsRepository personSettingsRepository;
    private final CitiesRepository citiesRepository;
    private final CountriesRepository countriesRepository;
    private final CaptchaRepository captchaRepository;
    private final CloudaryService cloudaryService;
    private final PersonMapper personMapper;
    private final SearchPersons searchPersons;

    public StorageRs storeImage(MultipartFile photo) throws IOException {
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        long personId = person.getId();
        StorageRs response = new StorageRs();
        StorageDataRs dataRs = new StorageDataRs();

        String originFileName = photo.getOriginalFilename();
        String extention = originFileName.substring(originFileName.lastIndexOf(".") + 1);
        String newFileName = "image" + personId + "." + extention;
        String newFileNameShort = "image" + personId;
        if (!extention.equals("jpg") && !extention.equals("png")) {
            response.setError("Отправлен файл не формата изображение jpg, png");
        }
        long fileLenth = photo.getSize();
        if (fileLenth > MAX_IMAGE_LENTH) {
            response.setError("Размер файла превышает допустимый размер");
        }

        if (response.getError() == null) {
            response.setTimestamp(0);
            dataRs.setBytes(0);
            dataRs.setCreatedAt(0);

            BufferedImage bufferedImage = ImageIO.read(photo.getInputStream());
            File outputfile = new File(newFileName);
            ImageIO.write(bufferedImage, extention, outputfile);

            cloudaryService.uploadImage(outputfile);
            outputfile.delete();
            String relativePath = cloudaryService.getImage(newFileNameShort);
            dataRs.setId(String.valueOf(personId));
            dataRs.setOwnerId(personId);
            dataRs.setFileName(newFileName);
            dataRs.setRelativeFilePath(relativePath);
            dataRs.setFileFormat(photo.getContentType());
            dataRs.setFileType(extention);
            response.setData(dataRs);

            person.setPhoto(relativePath);
            personsRepository.save(person);
        }
        return response;
    }

    public UserRs editProfile(UserRq userRq) {
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        UserRs response = new UserRs();
        PersonResponse personResponse = personMapper.toPersonResponse(person);
        if (userRq.getAbout() != null) {
            person.setAbout(userRq.getAbout());
            personResponse.setAbout(userRq.getAbout());
        }
        if (userRq.getBirth_date() != null) {
            person.setBirthDate(LocalDateTime.from(OffsetDateTime.parse(userRq.getBirth_date())));
            personResponse.setBirthDate(LocalDateTime.from(OffsetDateTime.parse(userRq.getBirth_date())));
        }
        if (userRq.getCity() != null) {
            person.setCity(userRq.getCity());
            personResponse.setCity(userRq.getCity());
            if (!citiesRepository.existsCityByTitle(userRq.getCity())) {
                City city = new City();
                city.setTitle(userRq.getCity());
                citiesRepository.save(city);
            }
        }
        if (userRq.getCountry() != null) {
            person.setCountry(userRq.getCountry());
            personResponse.setCountry(userRq.getCountry());
            if (!countriesRepository.existsCountryByTitle(userRq.getCountry())){
                Country country = new Country();
                country.setTitle(userRq.getCountry());
                countriesRepository.save(country);
            }
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

    public CommonResponse<List<PersonResponse>> findPersons(FindPersonRq personRq, int offset, int perPage) throws SQLException, EmptyFieldException {
        if (personRq.getFirst_name() == null && personRq.getLast_name() == null && personRq.getAge_from() == null
                && personRq.getAge_to() == null && personRq.getCity() == null && personRq.getCountry() == null) {
            throw new EmptyFieldException("All fields in query are empty");
        }
        return buildCommonResponse(offset, perPage, searchPersons.findPersons(personRq, offset, perPage), searchPersons.getTotal());
    }

    private CommonResponse<List<PersonResponse>> buildCommonResponse(int offset, int perPAge, List<Person> persons, long total) {
        return CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .data(personsToResponse(persons))
                .offset(offset)
                .perPage(perPAge)
                .total(total)
                .build();
    }
    public ResponseRsComplexRs deleteProfile(){
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        ResponseRsComplexRs response = new ResponseRsComplexRs();
        ComplexRs data = ComplexRs.builder()
                .id(0)
                .count(0)
                .message("OK")
                .message_id(0L)
                .build();
        response.setData(data);
        response.setTimestamp(0);
        response.setOffset(0);
        response.setPerPage(0);
        response.setError(null);
        response.setError_description(null);

        person.setIsDeleted(true);
        person.setDeletedTime(LocalDateTime.now());
        personsRepository.save(person);

        return response;
    }

    public ResponseRsComplexRs recoverProfile(){
        Person person = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        ResponseRsComplexRs response = new ResponseRsComplexRs();
        ComplexRs data = ComplexRs.builder()
                .id(0)
                .count(0)
                .message("OK")
                .message_id(0L)
                .build();
        response.setData(data);
        response.setTimestamp(0);
        response.setOffset(0);
        response.setPerPage(0);
        response.setError(null);
        response.setError_description(null);

        person.setIsDeleted(false);
        person.setDeletedTime(null);
        personsRepository.save(person);

        return response;
    }
    @Scheduled(fixedRateString = "${user.time-to-delete}")
    public void executeOldDeletes() {
        List<Long> idToDelete = personsRepository.findIdtoDelete(timeToDel);
        for(long oldId: idToDelete){
            blockHistoriesRepository.deleteAll(blockHistoriesRepository.findBHtoDelete(oldId));
            friendshipsRepository.deleteAll(friendshipsRepository.findFriendsToDelete(oldId));
            postsRepository.deleteAll(postsRepository.findPostsToDelete(oldId));
            List<Long> commentsIdToDelete = commentsRepository.findCommentsIdToDelete(oldId);
            commentsRepository.commentsDelete(oldId);
            for (long oldCommentId: commentsIdToDelete){
                commentsRepository.secondaryCommentsDelete(oldCommentId);
            }
            personSettingsRepository.persSetDelete(oldId);
            dialogsRepository.dialogsDelete(oldId);
            notificationsRepository.notificationDelete(oldId);
            messagesRepository.messagesDelete(oldId);
            likesRepository.likeDelete(oldId);
            personsRepository.deleteAll(personsRepository.findOldDeletes(timeToDel));
        }
    }

    private List<PersonResponse> personsToResponse(List<Person> persons) {
        return persons.stream().map(personMapper::toPersonResponse).collect(Collectors.toList());
    }
}
