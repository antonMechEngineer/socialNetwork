package soialNetworkApp.service;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.PersonSettings;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static soialNetworkApp.model.enums.FriendshipStatusTypes.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class FriendsServiceTest {

    @Autowired
    private FriendsService friendsService;

    @MockBean
    private NotificationsService notificationsService;

    @MockBean
    private FriendshipsRepository friendshipsRepository;

    @MockBean
    private PersonsRepository personsRepository;

    //TODO: данного репозитория больше не существует
//    @MockBean
//    private FriendshipStatusesRepository friendshipStatusesRepository;

    @MockBean
    private PersonMapper personMapper;

    private Friendship fsCurPsRcFr;
    private Friendship fsRcFr;
    private Friendship fsCurPsFr;
    private Friendship fsFr;
    private Friendship fsCurPsRqFr;
    private Friendship fsRqFr;

    private final static String CURRENT_PERSON_MAIL = "rhoncus.nullam@yahoo.edu";
    private final static String C_FRIEND_MAIL = "molestie@yahoo.edu";
    private final static String RECEIVED_FRIEND_MAIL = "nostra.per.inceptos@hotmail.com";
    private final static String REQUESTED_FRIEND_MAIL = "urna.suscipit@outlook.com";
    private final static String UNKNOWN_PERSON_MAIL = "blandit.congue@hotmail.couk";

    private final static Person CURRENT_PERSON = new Person(1L, CURRENT_PERSON_MAIL);
    private final static Person C_FRIEND = new Person(2L, C_FRIEND_MAIL);
    private final static Person RECEIVED_FRIEND = new Person(3L, RECEIVED_FRIEND_MAIL);
    private final static Person REQUESTED_FRIEND = new Person(4L, REQUESTED_FRIEND_MAIL);
    private final static Person UNKNOWN_PERSON = new Person(5L, UNKNOWN_PERSON_MAIL);

    private final static PersonRs C_FRIEND_DTO = PersonRs.builder().id(2L).email(C_FRIEND_MAIL).build();
    private final static PersonRs RECEIVED_FRIEND_DTO = PersonRs.builder().id(3L).email(RECEIVED_FRIEND_MAIL).build();

    private static final LocalDateTime TIME = LocalDateTime.now();
    private static final Integer OFFSET = 0;
    private static final Integer SIZE = 5;
    private static final Pageable PAGEABLE = PageRequest.of(OFFSET, SIZE);
    private static final ArrayList<Person> FRIENDS = new ArrayList<>(Arrays.asList(C_FRIEND));
    private static final Page<Person> PAGE_FRIENDS = new PageImpl<>(FRIENDS, PAGEABLE, FRIENDS.size());
    private static final ArrayList<Person> RECEIVED_FRIENDS = new ArrayList<>(Arrays.asList(RECEIVED_FRIEND));
    private static final Page<Person> PAGE_RECEIVED_FRIENDS = new PageImpl<>(RECEIVED_FRIENDS, PAGEABLE, RECEIVED_FRIENDS.size());
    private PersonSettings personSettings;

    @BeforeEach
    void setUp() {
        mockSecurityContext();
        buildFriendObjects();
        buildReceivedRequestObjects();
        buildRequestReceivedObjects();
        mockPersonsRepository();
        mockFriendshipsRepository();
        mockPersonMapper();
        personSettings = new PersonSettings();
        personSettings.setFriendRequestNotification(true);
        REQUESTED_FRIEND.setPersonSettings(personSettings);
    }

    private void mockSecurityContext(){
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getName()).thenReturn(CURRENT_PERSON_MAIL);
    }

    private void buildFriendObjects() {  //testDeleteFriend  // getFriends
        fsCurPsFr = new Friendship(1L, TIME, CURRENT_PERSON, C_FRIEND, FRIEND);
        fsFr = new Friendship(2L, TIME, C_FRIEND, CURRENT_PERSON, FRIEND);
    }

    private void buildReceivedRequestObjects() { //testAddFriend   //getRequests
        fsCurPsRcFr = new Friendship(3L, TIME, CURRENT_PERSON, RECEIVED_FRIEND, RECEIVED_REQUEST);
        fsRcFr = new Friendship(4L, TIME, RECEIVED_FRIEND, CURRENT_PERSON, REQUEST);
    }

    private void buildRequestReceivedObjects() { //testDeleteSentRequest
        fsCurPsRqFr = new Friendship(5L, TIME, CURRENT_PERSON, REQUESTED_FRIEND, REQUEST);
        fsRqFr = new Friendship(6L, TIME, REQUESTED_FRIEND, CURRENT_PERSON, RECEIVED_REQUEST);
    }

    private void mockPersonsRepository() {
        when(personsRepository.findPersonByEmail(CURRENT_PERSON_MAIL)).thenReturn(Optional.of(CURRENT_PERSON));
        when(personsRepository.findPersonById(CURRENT_PERSON.getId())).thenReturn(Optional.of(CURRENT_PERSON));
        when(personsRepository.findPersonById(RECEIVED_FRIEND.getId())).thenReturn(Optional.of(RECEIVED_FRIEND));
        when(personsRepository.findPersonById(C_FRIEND.getId())).thenReturn(Optional.of(C_FRIEND));
        when(personsRepository.findPersonById(REQUESTED_FRIEND.getId())).thenReturn(Optional.of(REQUESTED_FRIEND));
        when(personsRepository.findPersonById(UNKNOWN_PERSON.getId())).thenReturn(Optional.of(UNKNOWN_PERSON));
        when(personsRepository.findPersonByIdIn(List.of(C_FRIEND.getId()), PAGEABLE)).thenReturn(PAGE_FRIENDS);
        when(personsRepository.findPersonByIdIn(List.of(RECEIVED_FRIEND.getId()), PAGEABLE)).thenReturn(PAGE_RECEIVED_FRIENDS);
    }

    private void mockFriendshipsRepository() {
        when(friendshipsRepository.findFriendshipBySrcPerson(CURRENT_PERSON)).thenReturn(List.of(fsCurPsFr, fsCurPsRcFr, fsCurPsRqFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(RECEIVED_FRIEND)).thenReturn(List.of(fsRcFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(C_FRIEND)).thenReturn(List.of(fsFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(REQUESTED_FRIEND)).thenReturn(List.of(fsRqFr));
    }

    private void mockPersonMapper() {
        when(personMapper.toPersonResponse(C_FRIEND)).thenReturn(C_FRIEND_DTO);
        when(personMapper.toPersonResponse(RECEIVED_FRIEND)).thenReturn(RECEIVED_FRIEND_DTO);
    }

    @Test
    void addFriend() throws Exception {
        friendsService.addFriend(RECEIVED_FRIEND.getId());
        assertEquals(FRIEND, fsCurPsFr.getFriendshipStatus());
        assertEquals(FRIEND, fsCurPsRcFr.getFriendshipStatus());
        //TODO: скорее всего удалить строчку внизу
        //verify(friendshipStatusesRepository, times(2)).save(any());
    }

    @Test
    void sendFriendshipRequest() throws Exception {
        friendsService.sendFriendshipRequest(REQUESTED_FRIEND.getId());
        verify(friendshipsRepository, times(2)).save(any());
        //TODO: скорее всего удалить строчку внизу
        //verify(friendshipStatusesRepository, times(2)).save(any());
        verify(notificationsService, times(1)).createNotification(any(), any());
    }

    @Test
    void deleteFriend() throws Exception {
        friendsService.deleteFriend(C_FRIEND.getId());
        verify(friendshipsRepository, times(1)).delete(fsCurPsFr);
        verify(friendshipsRepository, times(1)).delete(fsFr);
    }

    @Test
    void deleteSentFriendshipRequest() throws Exception {
        friendsService.deleteSentFriendshipRequest(REQUESTED_FRIEND.getId());
        verify(friendshipsRepository, times(1)).delete(fsCurPsRqFr);
        verify(friendshipsRepository, times(1)).delete(fsRqFr);
    }

    @Test
    void getFriends() throws Exception {
        CommonRs<List<PersonRs>> resFriends = friendsService.getFriends(OFFSET, SIZE);
        PersonRs resDto = resFriends.getData().get(0);
        assertEquals(FRIENDS.size(), resFriends.getData().size());
        assertEquals(C_FRIEND.getId(), resDto.getId());
        assertEquals(C_FRIEND.getEmail(), resDto.getEmail());
    }

    @Test
    void getRequestedPersons() throws Exception {
        CommonRs<List<PersonRs>> resReceivedFriends = friendsService.getRequestedPersons(OFFSET, SIZE);
        PersonRs resDto = resReceivedFriends.getData().get(0);
        assertEquals(RECEIVED_FRIENDS.size(), resReceivedFriends.getData().size());
        assertEquals(RECEIVED_FRIEND.getId(), resDto.getId());
        assertEquals(RECEIVED_FRIEND.getEmail(), resDto.getEmail());
    }

    @Test
    void getStatusTwoPersons() {
        FriendshipStatusTypes actualFriendshipStatusTypes = friendsService.getStatusTwoPersons(CURRENT_PERSON, C_FRIEND);
        assertEquals(FRIEND, actualFriendshipStatusTypes);
    }
}