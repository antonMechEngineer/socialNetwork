package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import soialNetworkApp.service.util.CurrentUser;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsRecommendationService {

    private final FriendshipsRepository friendshipsRepository;

    private final PersonsRepository personsRepository;

    private final PersonMapper personMapper;

    private final CurrentUser currentUser;

    public CommonRs<List<PersonRs>> getFriendsRecommendation() {
        Person me = currentUser.getPerson();
        Pageable page = PageRequest.of(0, 8);
        List<Long> excludedPersons = new ArrayList<>();
        excludedPersons.add(me.getId());
        excludedPersons.addAll(blockPersons(me));
        Page<Person> persons = personsRepository.getPersonByCityAndIdNotIn(page, me.getCity(), excludedPersons);
        if (me.getCity() == null || persons.getTotalElements() == 0) {
            persons = personsRepository.getPersonByIdNotInOrderByRegDateDesc(page, excludedPersons);
        }
        return buildCommonResponse(persons, 0, 8);
    }

    private CommonRs<List<PersonRs>> buildCommonResponse(Page<Person> persons, int offset, int perPage) {
        return CommonRs.<List<PersonRs>>builder()
                .timestamp(System.currentTimeMillis())
                .total(persons.getTotalElements())
                .offset(offset)
                .perPage(perPage)
                .data(personsToPersonResponses(persons.getContent()))
                .build();
    }

    private List<PersonRs> personsToPersonResponses(List<Person> persons) {
        List<PersonRs> personResponse = new ArrayList<>();
        for (Person person : persons) {
            personResponse.add(personMapper.toPersonResponse(person));
        }
        return personResponse;
    }

    private List<Long> blockPersons(Person me) {
        List<Friendship> friendships = friendshipsRepository.findFriendshipsByDstPersonIdAndFriendshipStatus(me.getId(), FriendshipStatusTypes.BLOCKED);
        List<Long> srcPersons = getSrcPersons(friendships);
        return new ArrayList<>(srcPersons);
    }

    private List<Long> getSrcPersons(List<Friendship> friendships) {
        List<Long> srcPersons = new ArrayList<>();
        friendships.forEach(friendship -> srcPersons.add(friendship.getSrcPerson().getId()));
        return srcPersons;
    }
}
