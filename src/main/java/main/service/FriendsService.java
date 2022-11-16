package main.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import main.api.response.FriendRs;
import main.api.response.FriendsRs;
import main.model.entities.Friendship;
import main.model.entities.FriendshipStatus;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FriendsService {

    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public FriendsService(FriendshipsRepository friendshipsRepository,
                          FriendshipStatusesRepository friendshipStatusesRepository,
                          PersonsRepository personsRepository,
                          JWTUtil jwtUtil) {

        this.friendshipsRepository = friendshipsRepository;
        this.friendshipStatusesRepository = friendshipStatusesRepository;
        this.personsRepository = personsRepository;
        this.jwtUtil = jwtUtil;
    }

    public FriendRs addFriend(String token, Long futureFriendId){
        Person currentPerson = getPersonByToken(token);
        Person futureFriend = personsRepository.findPersonById(futureFriendId);
        FriendshipStatus friendshipStatus = new FriendshipStatus(LocalDateTime.now(),
                                            FriendshipStatusTypes.FRIEND,
                                            "mock");
        friendshipStatusesRepository.save(friendshipStatus);
        // TODO: 16.11.2022 может быть придется повторно достать status, что бы он был c id
        Friendship friendship = new Friendship(friendshipStatus, LocalDateTime.now(),
                                                currentPerson, futureFriend);
        friendshipsRepository.save(friendship);

        return new FriendRs();
    }

    public FriendRs deleteFriend(String token, Long idDeletablePerson){
        Person currentPerson = getPersonByToken(token);
        Person deletablePerson = personsRepository.findPersonById(idDeletablePerson);
        Friendship currentFriendShip =
                friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(currentPerson, deletablePerson);
        FriendshipStatus friendshipStatus = friendshipStatusesRepository.;

        return new FriendRs();
    }

    public FriendsRs getFriends(String token){

        return new FriendsRs();
    }

    public FriendRs sendFriendshipRequest(String token, Long id){

        return new FriendRs();
    }

    public FriendRs deleteSentFriendshipRequest(String token, Long id){

        return new FriendRs();
    }

    public FriendsRs getPotentialFriends(String token){

        return new FriendsRs();
    }

    private Person getPersonByToken(String token){
        String personEmail = jwtUtil.extractUserName(token);
        Person currentPerson = personsRepository.findPersonByEmail(personEmail);
        return currentPerson;
    }
}
