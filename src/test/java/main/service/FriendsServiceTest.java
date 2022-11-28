package main.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.mappers.FriendMapper;
import main.model.entities.Friendship;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class FriendsServiceTest {

    @Autowired
    private FriendsService friendsService;

    @Autowired
    private FriendshipsRepository friendshipsRepository;

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private FriendMapper friendMapper;


    private final String defaultError = "ok";
    private final static String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaG9uY3VzLm51bGxhbUB5YWhvby5lZHUiLCJpYXQiOjE2Njg4NDk3MTAsImV4cCI6MTY3OTY0OTcxMH0.vZ3y_zEilhMJYyGjlezHeh_olbdiWuIRU5-VTq8V974";
    private final static Long ID_DELETABLE_FRIEND = Long.valueOf(2);
    private final static Long ID_FUTURE_FRIEND = Long.valueOf(3);
    private final static Long ID_CURRENT_PERSON = Long.valueOf(1);
    private final static Long ID_EX_POTENTIAL_FRIEND = Long.valueOf(6);
    private final static Long ID_POTENTIAL_FRIEND = Long.valueOf(7);


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    //часть тестов может упасть если подвяжут другую тестовую базу
    @Test
    void addFriend() {
        friendsService.addFriend(TOKEN, ID_FUTURE_FRIEND);
        Person currentPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        Person futureFriend = personsRepository.findPersonById(ID_FUTURE_FRIEND).orElseThrow();
        Friendship currentPersonFs = friendshipsRepository.findFriendshipBySrcPerson(currentPerson).stream().
                filter(fs -> fs.getDstPerson() == futureFriend).collect(Collectors.toList()).get(0);
        Friendship friendPersonFs = friendshipsRepository.findFriendshipBySrcPerson(futureFriend).stream().
                filter(fs -> fs.getDstPerson() == currentPerson).collect(Collectors.toList()).get(0);
        // assertEquals(currentPersonFs.getFriendshipStatus().getCode(), FriendshipStatusTypes.FRIEND);
        // assertEquals(friendPersonFs.getFriendshipStatus().getCode(), FriendshipStatusTypes.FRIEND);
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
        Person srcPerson = friendsService.getSrcPersonByToken(TOKEN);
        Person expectedPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        //assertEquals(expectedPerson, srcPerson)
    }

    @Test
    void getStatusTwoPersons() {
        Person firstPerson = personsRepository.findPersonById(ID_CURRENT_PERSON).orElseThrow();
        Person secondPerson = personsRepository.findPersonById(ID_DELETABLE_FRIEND).orElseThrow();
        FriendshipStatusTypes actualFriendshipStatusTypes = friendsService.getStatusTwoPersons(firstPerson, secondPerson);
        //assertEquals(FriendshipStatusTypes.FRIEND, actualFriendshipStatusTypes)

    }
}