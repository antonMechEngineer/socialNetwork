package main.service;

import lombok.AllArgsConstructor;
import main.api.response.FriendRs;
import main.api.response.FriendsRs;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendsService {
    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;

    public FriendRs addFriend(Long id){

        return new FriendRs();
    }

    public FriendRs deleteFriend(Long id){

        return new FriendRs();
    }

    public FriendsRs getFriends(){

        return new FriendRs();
    }

    public FriendsRs getFriendRecommendations(){

        return new FriendsRs();
    }

    public FriendRs sendFriendshipRequest(Long id){

        return new FriendsRs();
    }

    public FriendsRs getPotentialFriends(){

        return new FriendsRs();
    }

}
