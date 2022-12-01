package main.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.mappers.FriendMapper;
import main.model.entities.Friendship;
import main.model.entities.FriendshipStatus;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static main.model.enums.FriendshipStatusTypes.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class FriendsServiceTest {

    @Autowired
    private FriendsService friendsService;

    @MockBean
    private FriendshipsRepository friendshipsRepository;

    @MockBean
    private PersonsRepository personsRepository;

    @Autowired
    private FriendMapper friendMapper;


    private FriendshipStatus fsStatusCurPsFtrFr;
    private FriendshipStatus fsStatusFtrFr;
    private Friendship fsCurPsFtrFr;
    private Friendship fsFtrFr;

    private FriendshipStatus fsStatusCurPsFr;
    private FriendshipStatus fsStatusFr;
    private Friendship fsCurPsFr;
    private Friendship fsFr;

    private FriendshipStatus fsStatusCurPsPtntlFr;
    private FriendshipStatus fsStatusPtntlFr;
    private Friendship fsCurPsPtntlFr;
    private Friendship fsPtntlFr;

    private FriendshipStatus fsStatusCurPsUnknown;
    private FriendshipStatus fsStatusPsUnknown;
    private Friendship fsCurPsUnknown;
    private Friendship fsPsUnknown;

    private ArrayList<Person> mockPersonRepo = new ArrayList<>();
    private ArrayList<Friendship> mockFriendshipRepo =  new ArrayList<>();
    private ArrayList<FriendshipStatus> mockFriendshipStatusRepo = new ArrayList<>();

    private PersonResponse frPersonResponse;
    private PersonResponse ptntlFrPersonResponse;


    private final static String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaG9uY3VzLm51bGxhbUB5YWhvby5lZHUiLCJpYXQiOjE2Njg4NDk3MTAsImV4cCI6MTY3OTY0OTcxMH0.vZ3y_zEilhMJYyGjlezHeh_olbdiWuIRU5-VTq8V974";

    private final static String CURRENT_PERSON_MAIL = "rhoncus.nullam@yahoo.edu";
    private final static String C_FRIEND_MAIL = "molestie@yahoo.edu";
    private final static String RECEIVED_FRIEND_MAIL = "nostra.per.inceptos@hotmail.com";
    private final static String RESPONSE_FRIEND_MAIL = "urna.suscipit@outlook.com";
    private final static String UNKNOWN_PERSON_MAIL = "blandit.congue@hotmail.couk";

    private final static Person CURRENT_PERSON = new Person(1L,  CURRENT_PERSON_MAIL);
    private final static Person C_FRIEND = new Person(2L,  C_FRIEND_MAIL);
    private final static Person RECEIVED_FRIEND = new Person(3L,  RECEIVED_FRIEND_MAIL);
    private final static Person RESPONSE_FRIEND = new Person(4L,  RESPONSE_FRIEND_MAIL);
    private final static Person UNKNOWN_PERSON = new Person(5L,  UNKNOWN_PERSON_MAIL);

    private static final Integer OFFSET = 0;
    private static final Integer SIZE = 5;
    private static final Pageable PAGEABLE = PageRequest.of(OFFSET, SIZE);
    private static final ArrayList<Person> FRIENDS = new ArrayList<>(Arrays.asList(C_FRIEND));
    private static final Page<Person> PAGE_FRIENDS = new PageImpl<>(FRIENDS, PAGEABLE, FRIENDS.size());
    private static final ArrayList<Person> RECEIVED_FRIENDS = new ArrayList<>(Arrays.asList(RECEIVED_FRIEND));
    private static final Page<Person> PAGE_RECEIVED_FRIENDS = new PageImpl<>(RECEIVED_FRIENDS, PAGEABLE, RECEIVED_FRIENDS.size());

    private static final LocalDateTime TIME = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        buildFriendObjects();
        buildReceivedRequestObjects();
        buildRequestReceivedObjects();
        buildMockRepos();
        mockPersonsRepository();
        mockFriendshipsRepository();
        // TODO: 29.11.2022 создать свои листы репозитории, которые связать с моковыми действиями в реальных репозиториях
    }

    void buildMockRepos(){
        mockPersonRepo.clear();
        mockFriendshipRepo.clear();
        mockFriendshipStatusRepo.clear();
        mockPersonRepo.addAll(Arrays.asList(CURRENT_PERSON, C_FRIEND, RECEIVED_FRIEND, RESPONSE_FRIEND, UNKNOWN_PERSON));
        mockFriendshipRepo.addAll(Arrays.asList(fsCurPsFr, fsFr, fsCurPsFtrFr, fsFtrFr, fsCurPsPtntlFr, fsPtntlFr));
        mockFriendshipStatusRepo.addAll(Arrays.asList(fsStatusCurPsFr, fsStatusFr, fsStatusCurPsFtrFr,
                                                        fsStatusFtrFr, fsStatusCurPsPtntlFr, fsStatusPtntlFr));
    }

    void buildFriendObjects(){  //testDeleteFriend  // getFriends
        fsStatusCurPsFr = new FriendshipStatus(1L, TIME, C_FRIEND.toString(), FRIEND);
        fsStatusFr = new FriendshipStatus(2L, TIME, C_FRIEND.toString(), FRIEND);
        fsCurPsFr = new Friendship(1L, TIME,CURRENT_PERSON, C_FRIEND, fsStatusCurPsFr);
        fsFr = new Friendship(2L, TIME, C_FRIEND, CURRENT_PERSON, fsStatusFr);
    }

    void buildReceivedRequestObjects(){ //testAddFriend   //getRequests
        fsStatusCurPsFtrFr = new FriendshipStatus(3L, TIME, RECEIVED_REQUEST.toString(), RECEIVED_REQUEST);
        fsStatusFtrFr = new FriendshipStatus(4L, TIME, REQUEST.toString(), REQUEST);
        fsCurPsFtrFr = new Friendship(3L, TIME, CURRENT_PERSON, RECEIVED_FRIEND, fsStatusCurPsFtrFr);
        fsFtrFr = new Friendship(4L, TIME, RECEIVED_FRIEND, CURRENT_PERSON, fsStatusFtrFr);
    }

    void buildRequestReceivedObjects(){ //testDeleteSentRequest
        fsStatusCurPsPtntlFr = new FriendshipStatus(5L, TIME, REQUEST.toString(), REQUEST);
        fsStatusPtntlFr = new FriendshipStatus(6L, TIME, RECEIVED_REQUEST.toString(), RECEIVED_REQUEST);
        fsCurPsPtntlFr = new Friendship(5L, TIME, CURRENT_PERSON, RESPONSE_FRIEND, fsStatusCurPsFtrFr);
        fsPtntlFr = new Friendship(6L, TIME, RESPONSE_FRIEND, CURRENT_PERSON, fsStatusFtrFr);
    }

    void mockPersonsRepository() {
        when(personsRepository.findPersonByEmail(CURRENT_PERSON_MAIL)).thenReturn(Optional.of(CURRENT_PERSON));
        when(personsRepository.findPersonById(CURRENT_PERSON.getId())).thenReturn(Optional.of(CURRENT_PERSON));
        when(personsRepository.findPersonById(RECEIVED_FRIEND.getId())).thenReturn(Optional.of(RECEIVED_FRIEND));
        when(personsRepository.findPersonById(C_FRIEND.getId())).thenReturn(Optional.of(C_FRIEND));
        when(personsRepository.findPersonById(RESPONSE_FRIEND.getId())).thenReturn(Optional.of(RESPONSE_FRIEND));
        when(personsRepository.findPersonById(UNKNOWN_PERSON.getId())).thenReturn(Optional.of(UNKNOWN_PERSON));
        when(personsRepository.findPersonBySrcFriendshipsIn(FRIENDS, PAGEABLE)).thenReturn(PAGE_FRIENDS);
        when(personsRepository.findPersonBySrcFriendshipsIn(RECEIVED_FRIENDS, PAGEABLE)).thenReturn(PAGE_RECEIVED_FRIENDS);
    }
    void mockFriendshipsRepository(){
        when(friendshipsRepository.findFriendshipBySrcPerson(CURRENT_PERSON)).thenReturn(List.of(fsCurPsFr, fsCurPsFtrFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(RECEIVED_FRIEND)).thenReturn(List.of(fsFtrFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(C_FRIEND)).thenReturn(List.of(fsFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(RESPONSE_FRIEND)).thenReturn(List.of(fsPtntlFr));

//        when(friendMapper.toFriendResponse(currentPerson, FRIEND)).thenReturn();
//        when(friendMapper.toFriendResponse(currentPerson, RECEIVED_REQUEST)).thenReturn();
        //when(friendshipsRepository.delete(fsFr)).thenAnswer(mockFriendshipRepo.remove(fsFr)); //не понятно как мокнуть без возврата см. др. тестовые классы, в дипломе я подобное делал
    }

    // TODO: 30.11.2022 проверить вызывается ли метод save, возможно ли объявлять метод when который будет вызываться одинаково в рамках всего класса
    @Test
    void addFriend() {
        friendsService.addFriend(TOKEN, RECEIVED_FRIEND.getId());
        assertEquals(C_FRIEND, fsCurPsFr.getFriendshipStatus().getCode());
        assertEquals(C_FRIEND, fsCurPsFtrFr.getFriendshipStatus().getCode());
    }

    @Test
    void sendFriendshipRequest() {
        friendsService.sendFriendshipRequest(TOKEN, RESPONSE_FRIEND.getId());
        //mockFriendshipRepo.save д.б
        // assertEquals(REQUEST, currentPersonFs.getFriendshipStatus().getCode());
        // assertEquals(RECEIVED_REQUEST, dstPersonFs.getFriendshipStatus().getCode());
    }

    @Test
    void deleteFriend() {
        friendsService.deleteFriend(TOKEN, C_FRIEND.getId());
        //проверить вызываются ли методы delete
        //assertEquals(false, isContainDeletablePerson)
    }

    @Test
    void deleteSentFriendshipRequest() {
        friendsService.deleteSentFriendshipRequest(TOKEN, RESPONSE_FRIEND.getId());
        //проверить вызываются ли методы delete
        //assertEquals(false, isContainExPotentialPerson)
    }

    @Test
    void getFriends() {
        CommonResponse<List<PersonResponse>> resFriends = friendsService.getFriends(TOKEN, OFFSET, SIZE);
        PersonResponse resDto =  resFriends.getData().get(0);
        assertEquals(FRIENDS.size(), resFriends.getData().size());
        assertEquals(C_FRIEND.getId(), resDto.getId());
        assertEquals(C_FRIEND.getEmail(), resDto.getEmail());
    }

    @Test
    void getRequestedPersons() {
        CommonResponse<List<PersonResponse>> resReceivedFriends = friendsService.getRequestedPersons(TOKEN, OFFSET, SIZE);
        PersonResponse resDto =  resReceivedFriends.getData().get(0);
        assertEquals(RECEIVED_FRIENDS.size(), resReceivedFriends.getData().size());
        assertEquals(RECEIVED_FRIEND.getId(), resDto.getId());
        assertEquals(RECEIVED_FRIEND.getEmail(), resDto.getEmail());
    }

    @Test
    void getSrcPersonByToken() {
        Person srcPerson = friendsService.getSrcPersonByToken(TOKEN);
        Person expectedPerson = personsRepository.findPersonById(CURRENT_PERSON.getId()).orElseThrow();
        assertEquals(expectedPerson, srcPerson);
    }

    @Test
    void getStatusTwoPersons() {
        FriendshipStatusTypes actualFriendshipStatusTypes = friendsService.getStatusTwoPersons(CURRENT_PERSON, C_FRIEND);
        assertEquals(FRIEND, actualFriendshipStatusTypes);
    }
}