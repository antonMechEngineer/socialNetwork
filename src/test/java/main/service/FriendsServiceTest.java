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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private Person currentPerson;
    private Person currFriend;
    private Person futureFriend;
    private Person potentialFriend;
    private Person unknownPerson;

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

    private ArrayList<Person> mockPersonRepo;
    private ArrayList<Friendship> mockFriendshipRepo;
    private ArrayList<FriendshipStatus> mockFriendshipStatusRepo;

    private final static String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaG9uY3VzLm51bGxhbUB5YWhvby5lZHUiLCJpYXQiOjE2Njg4NDk3MTAsImV4cCI6MTY3OTY0OTcxMH0.vZ3y_zEilhMJYyGjlezHeh_olbdiWuIRU5-VTq8V974";
    private final static String EMAIL_CURRENT_PERSON = "rhoncus.nullam@yahoo.edu";
    private final static Long ID_CURRENT_PERSON = Long.valueOf(1);
    private final static Long ID_FRIEND = Long.valueOf(2);
    private final static Long ID_FUTURE_FRIEND = Long.valueOf(3);
    private final static Long ID_POTENTIAL_FRIEND = Long.valueOf(4);
    private final static Long ID_UNKNOWN_PERSON = Long.valueOf(5);
    private static final LocalDateTime TIME = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        currentPerson = new Person(ID_CURRENT_PERSON, EMAIL_CURRENT_PERSON);
        currFriend = new Person(ID_FRIEND);
        futureFriend = new Person(ID_FUTURE_FRIEND);
        potentialFriend = new Person(ID_POTENTIAL_FRIEND);
        unknownPerson = new Person(ID_UNKNOWN_PERSON);
        buildFriendObjects();
        buildReceivedRequestObjects();
        buildRequestReceivedObjects();
        buildMockRepos();
        buildMockAnswers();
        // TODO: 29.11.2022 создать свои листы репозитории, которые связать с моковыми действиями в реальных репозиториях
    }

    void buildMockRepos(){
        mockPersonRepo.clear();
        mockFriendshipRepo.clear();
        mockFriendshipStatusRepo.clear();
        mockPersonRepo.addAll(Arrays.asList(currentPerson, currFriend, futureFriend, potentialFriend, unknownPerson));
        mockFriendshipRepo.addAll(Arrays.asList(fsCurPsFr, fsFr, fsCurPsFtrFr, fsFtrFr, fsCurPsPtntlFr, fsPtntlFr));
        mockFriendshipStatusRepo.addAll(Arrays.asList(fsStatusCurPsFr, fsStatusFr, fsStatusCurPsFtrFr, fsStatusFtrFr, fsStatusCurPsPtntlFr, fsStatusPtntlFr));
    }

    void buildFriendObjects(){  //testDeleteFriend  // getFriends
        fsStatusCurPsFr = new FriendshipStatus(1L, TIME, FRIEND.toString(), FRIEND);
        fsStatusFr = new FriendshipStatus(2L, TIME, FRIEND.toString(), FRIEND);
        fsCurPsFr = new Friendship(1L, TIME,currentPerson, currFriend, fsStatusCurPsFtrFr);
        fsFr = new Friendship(2L, TIME, currentPerson, currentPerson, fsStatusFtrFr);
    }

    void buildReceivedRequestObjects(){ //testAddFriend   //getRequests
        fsStatusCurPsFtrFr = new FriendshipStatus(3L, TIME, RECEIVED_REQUEST.toString(), RECEIVED_REQUEST);
        fsStatusFtrFr = new FriendshipStatus(4L, TIME, REQUEST.toString(), FriendshipStatusTypes.REQUEST);
        fsCurPsFtrFr = new Friendship(3L, TIME, currentPerson, futureFriend, fsStatusCurPsFtrFr);
        fsFtrFr = new Friendship(4L, TIME, futureFriend, currentPerson, fsStatusFtrFr);
    }

    void buildRequestReceivedObjects(){ //testDeleteSentRequest
        fsStatusCurPsPtntlFr = new FriendshipStatus(5L, TIME, REQUEST.toString(), REQUEST);
        fsStatusPtntlFr = new FriendshipStatus(6L, TIME, RECEIVED_REQUEST.toString(), RECEIVED_REQUEST);
        fsCurPsPtntlFr = new Friendship(5L, TIME, currentPerson, potentialFriend, fsStatusCurPsFtrFr);
        fsPtntlFr = new Friendship(6L, TIME, potentialFriend, currentPerson, fsStatusFtrFr);
    }

    void buildMockAnswers(){
        when(personsRepository.findPersonByEmail(EMAIL_CURRENT_PERSON)).thenReturn(Optional.ofNullable(currentPerson));
        when(personsRepository.findPersonById(ID_CURRENT_PERSON)).thenReturn(Optional.ofNullable(currentPerson));
        when(personsRepository.findPersonById(ID_FUTURE_FRIEND)).thenReturn(Optional.ofNullable(futureFriend));
        when(personsRepository.findPersonById(ID_FRIEND)).thenReturn(Optional.ofNullable(currFriend));
        when(personsRepository.findPersonById(ID_POTENTIAL_FRIEND)).thenReturn(Optional.ofNullable(potentialFriend));
        when(personsRepository.findPersonById(ID_UNKNOWN_PERSON)).thenReturn(Optional.ofNullable(unknownPerson));

        when(friendshipsRepository.findFriendshipBySrcPerson(currentPerson)).thenReturn(List.of(fsCurPsFtrFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(futureFriend)).thenReturn(List.of(fsFtrFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(currFriend)).thenReturn(List.of(fsFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(potentialFriend)).thenReturn(List.of(fsPtntlFr));

        when(friendshipsRepository.delete(fsFr)).thenAnswer(mockFriendshipRepo.remove(fsFr)); //не понятно как мокнуть без возврата см. др. тестовые классы, в дипломе я подобное делал
    }

    // TODO: 30.11.2022 проверить вызывается ли метод save, возможно ли объявлять метод when который будет вызываться одинаково в рамках всего класса
    @Test
    void addFriend() {
        friendsService.addFriend(TOKEN, ID_FUTURE_FRIEND);
        assertEquals(FRIEND, fsCurPsFr.getFriendshipStatus().getCode());
        assertEquals(FRIEND, fsCurPsFtrFr.getFriendshipStatus().getCode());
    }

    @Test
    void sendFriendshipRequest() {
        friendsService.sendFriendshipRequest(TOKEN, ID_POTENTIAL_FRIEND);
        //mockFriendshipRepo.save д.б
        // assertEquals(REQUEST, currentPersonFs.getFriendshipStatus().getCode());
        // assertEquals(RECEIVED_REQUEST, dstPersonFs.getFriendshipStatus().getCode());
    }

    @Test
    void deleteFriend() {
        friendsService.deleteFriend(TOKEN, ID_FRIEND);
        //проверить вызываются ли методы delete
        //assertEquals(false, isContainDeletablePerson)
    }

    @Test
    void deleteSentFriendshipRequest() {
        friendsService.deleteSentFriendshipRequest(TOKEN, ID_POTENTIAL_FRIEND);
        //проверить вызываются ли методы delete
        //assertEquals(false, isContainExPotentialPerson)
    }

    @Test
    void getFriends() {
        Person srcPerson = friendsService.getSrcPersonByToken(TOKEN);
        CommonResponse<List<PersonResponse>> resFriends = friendsService.getFriends(TOKEN);
        List<Person> personsEntity = personsRepository.findAll();
        List<PersonResponse> expectedFriendsDto = new ArrayList<>();
        for (Person person : personsEntity) {
            expectedFriendsDto.add(friendMapper.toFriendResponse(person, friendsService.getStatusTwoPersons(person, srcPerson)));
        }
        //assertEquals(expectedFriendsDto, actualFriendsDto)
    }

    @Test
    void getRequestedPersons() {
        CommonResponse<List<PersonResponse>> resRequestedPersons = friendsService.getFriends(TOKEN);
        //схема такая же как и в getFriends
    }

    @Test
    void getSrcPersonByToken() {
        Person srcPerson = friendsService.getSrcPersonByToken(TOKEN);
        Person expectedPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        assertEquals(expectedPerson, srcPerson);
    }

    @Test
    void getStatusTwoPersons() {
        FriendshipStatusTypes actualFriendshipStatusTypes = friendsService.getStatusTwoPersons(currentPerson, currFriend);
        assertEquals(FRIEND, actualFriendshipStatusTypes);

    }
}