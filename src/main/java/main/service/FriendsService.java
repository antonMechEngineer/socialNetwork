package main.service;

import lombok.AllArgsConstructor;
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

    public void addFriend(Long id){

    }

    public void deleteFriend(Long id){

    }

    public void getFriends(){

    }

}
