package main.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.mappers.FriendMapper;
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

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class FriendsServiceTest {

    @Autowired
    private FriendsService friendsService;

    private FriendshipsRepository friendshipsRepository;
    private FriendshipStatusesRepository friendshipStatusesRepository;
    private PersonsRepository personsRepository;
    private JWTUtil jwtUtil;
    private FriendMapper friendMapper;
    private final String defaultError = "ok";
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaG9uY3VzLm51bGxhbUB5YWhvby5lZHUiLCJpYXQiOjE2Njg4NDk3MTAsImV4cCI6MTY3OTY0OTcxMH0.vZ3y_zEilhMJYyGjlezHeh_olbdiWuIRU5-VTq8V974";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addFriend() {

    }

    @Test
    void sendFriendshipRequest() {
    }

    @Test
    void deleteFriend() {
    }

    @Test
    void deleteSentFriendshipRequest() {
    }

    @Test
    void getFriends() {
    }

    @Test
    void getRequestedPersons() {
    }
}