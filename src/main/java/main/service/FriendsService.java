package main.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import main.api.response.FriendRs;
import main.api.response.FriendsRs;
import main.model.entities.Friendship;
import main.model.entities.FriendshipStatus;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendsService {

    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;

    @Autowired
    public FriendsService(FriendshipsRepository friendshipsRepository,
                          FriendshipStatusesRepository friendshipStatusesRepository,
                          PersonsRepository personsRepository) {

        this.friendshipsRepository = friendshipsRepository;
        this.friendshipStatusesRepository = friendshipStatusesRepository;
        this.personsRepository = personsRepository;
    }

    public FriendRs addFriend(Long id){
        Friendship friendship = new Friendship();
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        return new FriendRs();
    }

    public FriendRs deleteFriend(Long id){
        Friendship friendship = new Friendship();
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        return new FriendRs();
    }

    public FriendsRs getFriends(){

        return new FriendsRs();
    }

    public FriendRs sendFriendshipRequest(Long id){

        return new FriendRs();
    }

    public FriendRs deleteSentFriendshipRequest(Long id){

        return new FriendRs();
    }

    public FriendsRs getPotentialFriends(){

        return new FriendsRs();
    }

}
