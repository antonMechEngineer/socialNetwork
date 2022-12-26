package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.errors.UserPageBlockedException;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonsService {

    private final FriendshipsRepository friendshipsRepository;

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;
    private final FriendsService friendsService;

    public CommonRs<PersonRs> getPersonDataById(Long id) throws UserPageBlockedException {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        blockUserPage(srcPerson, id);
        Person person = getPersonById(id);
        person.setFriendStatus(friendsService.getStatusTwoPersons(person, srcPerson));
        return getCommonPersonResponse(person);
    }

    public CommonRs<PersonRs> getMyData() {
        return getCommonPersonResponse(getPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public Person getPersonById(long personId) {
        return personsRepository.findById(personId).orElse(null);
    }

    public Person getPersonByEmail(String email) {
        return personsRepository.findPersonByEmail(email).orElse(null);
    }

    public Person getPersonByContext() {
        return personsRepository.findPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElse(null);
    }

    public boolean validatePerson(Person person) {
        return person != null && person.equals(getPersonByContext());
    }

    private CommonRs<PersonRs> getCommonPersonResponse(Person person) {
        return CommonRs.<PersonRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(personMapper.toPersonResponse(person))
                .build();
    }

    private void blockUserPage(Person me, Long userWhoBlocked) throws UserPageBlockedException {
        List<Friendship> friendships = friendshipsRepository.findFriendshipsByDstPersonIdAndFriendshipStatus(me.getId(), FriendshipStatusTypes.BLOCKED);
        List<Long> srcPersonsIds = getSrcPersons(friendships);
        if (srcPersonsIds.contains(userWhoBlocked)) {
            throw new UserPageBlockedException("User page with id: '" + userWhoBlocked + "' blocked for person with id: '" + me.getId() + "'");
        }
    }

    private List<Long> getSrcPersons (List<Friendship> friendships) {
        List<Long> ids = new ArrayList<>();
        friendships.forEach(friendship -> ids.add(friendship.getSrcPerson().getId()));
        return ids;
    }
}
