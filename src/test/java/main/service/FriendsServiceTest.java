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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
    private Person deletableFriend;
    private Person futureFriend;
    private Person exPotentialFriend;
    private Person potentialFriend;

    private FriendshipStatus fsStatusCurPsFutureFr;
    private FriendshipStatus fsStatusFutureFr;
    private Friendship fsCurPersonFutureFr;
    private Friendship fsFutureFr;

    private FriendshipStatus fsStatusCurPsDeletableFriend;
    private FriendshipStatus fsStatusDeletableFriend;
    private Friendship fsCurPersonDeletableFriend;
    private Friendship fsDeletablePerson;

    private FriendshipStatus fsStatusCurPsPotentialFr;
    private FriendshipStatus fsStatusPotentialFr;
    private Friendship fsCurPersonPotentialFr;
    private Friendship getFsCurPersonPotentialFr;

    private FriendshipStatus fsStatusCurPsExPotentialFr;
    private FriendshipStatus fsStatusExPotentialFr;
    private Friendship fsCurPersonExPotentialFr;
    private Friendship fsExPotentialFriend;

    private final static String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaG9uY3VzLm51bGxhbUB5YWhvby5lZHUiLCJpYXQiOjE2Njg4NDk3MTAsImV4cCI6MTY3OTY0OTcxMH0.vZ3y_zEilhMJYyGjlezHeh_olbdiWuIRU5-VTq8V974";
    private final static String EMAIL_CURRENT_PERSON = "rhoncus.nullam@yahoo.edu";
    private final static Long ID_CURRENT_PERSON = Long.valueOf(1);
    private final static Long ID_FRIEND = Long.valueOf(2);
    private final static Long ID_DELETABLE_FRIEND = Long.valueOf(3);
    private final static Long ID_FUTURE_FRIEND = Long.valueOf(4);
    private final static Long ID_EX_POTENTIAL_FRIEND = Long.valueOf(5);
    private final static Long ID_POTENTIAL_FRIEND = Long.valueOf(6);

    @BeforeEach
    void setUp() {
        currFriend = new Person();
        currFriend.setId(ID_FRIEND);
        deletableFriend = new Person();
        deletableFriend.setId(ID_DELETABLE_FRIEND);
        futureFriend = new Person();
        futureFriend.setId(ID_FUTURE_FRIEND);
        exPotentialFriend = new Person();
        exPotentialFriend.setId(ID_EX_POTENTIAL_FRIEND);
        potentialFriend = new Person();
        potentialFriend.setId(ID_POTENTIAL_FRIEND);
        currentPerson = new Person();
        currentPerson.setId(ID_CURRENT_PERSON);
        currentPerson.setEmail(EMAIL_CURRENT_PERSON);

        fsStatusCurPsDeletableFriend = new FriendshipStatus(1L, LocalDateTime.now(),
                FriendshipStatusTypes.FRIEND.toString(), FriendshipStatusTypes.FRIEND);
        fsStatusDeletableFriend = new FriendshipStatus(2L, LocalDateTime.now(),
                FriendshipStatusTypes.FRIEND.toString(), FriendshipStatusTypes.FRIEND);
        fsCurPersonDeletableFriend = new Friendship(1L, LocalDateTime.now(),currentPerson, futureFriend, fsStatusCurPsFutureFr);
        fsDeletablePerson = new Friendship(2L, LocalDateTime.now(), futureFriend, currentPerson, fsStatusFutureFr);

        fsStatusCurPsFutureFr = new FriendshipStatus(3L, LocalDateTime.now(),
                FriendshipStatusTypes.RECEIVED_REQUEST.toString(), FriendshipStatusTypes.RECEIVED_REQUEST);
        fsStatusFutureFr = new FriendshipStatus(4L, LocalDateTime.now(),
                FriendshipStatusTypes.REQUEST.toString(), FriendshipStatusTypes.REQUEST);
        fsCurPersonFutureFr = new Friendship(3L, LocalDateTime.now(),currentPerson, futureFriend, fsStatusCurPsFutureFr);
        fsFutureFr = new Friendship(4L, LocalDateTime.now(), futureFriend, currentPerson, fsStatusFutureFr);
        // TODO: 29.11.2022   нужно билдить базу в отдельных метода, создать свои листы репозитории, которые связать с моковыми действиями в реальных репозиториях

    }

    @Test
    void addFriend() {
        when(personsRepository.findPersonById(ID_CURRENT_PERSON)).thenReturn(Optional.ofNullable(currentPerson));
        when(personsRepository.findPersonById(ID_FUTURE_FRIEND)).thenReturn(Optional.ofNullable(futureFriend));
        when(friendshipsRepository.findFriendshipBySrcPerson(currentPerson)).thenReturn(List.of(fsCurPersonFutureFr));
        when(friendshipsRepository.findFriendshipBySrcPerson(futureFriend)).thenReturn(List.of(fsFutureFr));
        friendsService.addFriend(TOKEN, ID_FUTURE_FRIEND);

        assertEquals(currentPersonFs.getFriendshipStatus().getCode(), FriendshipStatusTypes.FRIEND);
        assertEquals(friendPersonFs.getFriendshipStatus().getCode(), FriendshipStatusTypes.FRIEND);
    }

    @Test
    void sendFriendshipRequest() {
        friendsService.sendFriendshipRequest(TOKEN, ID_POTENTIAL_FRIEND);
        Person currentPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        Person potentialFriend = personsRepository.findPersonById(ID_DELETABLE_FRIEND).orElseThrow();
        Friendship currentPersonFs = friendshipsRepository.findFriendshipBySrcPerson(currentPerson).stream().
                filter(fs -> fs.getDstPerson() == potentialFriend).collect(Collectors.toList()).get(0);
        Friendship dstPersonFs = friendshipsRepository.findFriendshipBySrcPerson(potentialFriend).stream().
                filter(fs -> fs.getDstPerson() == currentPerson).collect(Collectors.toList()).get(0);

        // assertEquals(currentPersonFs.getFriendshipStatus().getCode(), FriendshipStatusTypes.REQUEST);
        // assertEquals(dstPersonFs.getFriendshipStatus().getCode(), FriendshipStatusTypes.RECEIVED_REQUEST);
    }

    @Test
    void deleteFriend() {
        friendsService.deleteFriend(TOKEN, ID_DELETABLE_FRIEND);
        Person currentPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        Person deletablePerson = personsRepository.findPersonById(ID_DELETABLE_FRIEND).orElseThrow();
        Boolean isContainDeletablePerson = currentPerson.getDstFriendships().contains(deletablePerson);
        //assertEquals(false, isContainDeletablePerson)
    }

    @Test
    void deleteSentFriendshipRequest() {
        friendsService.deleteSentFriendshipRequest(TOKEN, ID_EX_POTENTIAL_FRIEND);
        Person currentPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        Person exPotentialFriend = personsRepository.findPersonById(ID_EX_POTENTIAL_FRIEND).orElseThrow();
        Boolean isContainExPotentialPerson = currentPerson.getDstFriendships().contains(exPotentialFriend);
        //assertEquals(false, isContainExPotentialPerson)
    }

    @Test
    void getFriends() {
        Person srcPerson = friendsService.getSrcPersonByToken(TOKEN);
        CommonResponse<List<PersonResponse>> resFriends = friendsService.getFriends(TOKEN);
        List<PersonResponse> actualFriendsDto = resFriends.getData();
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
        when(personsRepository.findPersonByEmail(EMAIL_CURRENT_PERSON)).thenReturn(Optional.of(currentPerson));
        when(personsRepository.findPersonById(ID_CURRENT_PERSON)).thenReturn(Optional.of(currentPerson));
        Person srcPerson = friendsService.getSrcPersonByToken(TOKEN);
        Person expectedPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        assertEquals(expectedPerson, srcPerson);
    }

    @Test
    void getStatusTwoPersons() {
        when(personsRepository.findPersonById(ID_CURRENT_PERSON)).thenReturn(Optional.of(currentPerson));
        when(personsRepository.findPersonById(ID_DELETABLE_FRIEND)).thenReturn(Optional.of(deletableFriend));

        FriendshipStatus mockFriendshipStatus = new FriendshipStatus(
                1L, LocalDateTime.now(), FriendshipStatusTypes.FRIEND.toString(), FriendshipStatusTypes.FRIEND);
        Friendship mockFriendship = new Friendship(1L, LocalDateTime.now(),currentPerson, deletableFriend, mockFriendshipStatus);
        when(friendshipsRepository.findFriendshipBySrcPerson(currentPerson)).thenReturn(List.of(mockFriendship));
        FriendshipStatusTypes actualFriendshipStatusTypes = friendsService.getStatusTwoPersons(currentPerson, deletableFriend);
        assertEquals(FriendshipStatusTypes.FRIEND, actualFriendshipStatusTypes);

    }
}