package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.mappers.FriendMapper;
import main.mappers.PersonMapper;
import main.model.entities.Friendship;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonsService {

    private final PersonsRepository personsRepository;
    private final CurrencyService currencyService;
    private final PersonMapper personMapper;
    private final FriendMapper friendMapper;
    private final FriendsService friendsService;
    private final FriendshipsRepository friendshipsRepository;

    public CommonResponse<PersonResponse> getPersonDataById(Long id, String token) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        return getCommonPersonResponse(getPersonById(id), srcPerson);
    }

    public CommonResponse<PersonResponse> getMyData() {
        return getCommonPersonResponse(getPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public UserRs editImage(Principal principal, MultipartFile photo, String phone, String about,
                            String city, String country, String first_name, String last_name,
                            String birth_date, String message_permission) throws IOException {
        Person person = personsRepository.findPersonByEmail(principal.getName()).get();
        UserRs response = new UserRs();

        return response;
    }

    public Person getPersonById(long personId) {
        return personsRepository.findById(personId).orElse(null);
    }

    public Person getPersonByEmail(String email) {
        return personsRepository.findPersonByEmail(email).orElse(null);
    }

    public Person getPersonByContext() {
        return personsRepository.findPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElse(null);
    }

    public boolean validatePerson(Person person) {
        return person != null && person.equals(getPersonByContext());
    }


    private CommonResponse<PersonResponse> getCommonPersonResponse(Person person, Person srcPerson) {
        return CommonResponse.<PersonResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(friendMapper.toFriendResponse(person, friendsService.getStatusTwoPersons(person, srcPerson)))
                .build();
    }

    private CommonResponse<PersonResponse> getCommonPersonResponse(Person person) {
        return CommonResponse.<PersonResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(personMapper.toPersonResponse(person))
                .build();
    }

    @EventListener
    public void updatePersonOnlineTime(SessionSubscribeEvent event) {
        MessageHeaders messageHeaders = event.getMessage().getHeaders();
        String personIdFromHeader = SimpMessageHeaderAccessor.getSubscriptionId(messageHeaders);
        Long personIdFromUser = personsRepository.findPersonId(SimpMessageHeaderAccessor.getUser(messageHeaders).getName()).orElse(0L);
        if (personIdFromUser.equals(Long.parseLong(personIdFromHeader))) {
            personsRepository.updateOnlineTime(Long.parseLong(personIdFromHeader));
        }
    }
}
