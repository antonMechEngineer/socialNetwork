package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.*;
import main.mappers.PersonMapper;
import main.model.entities.Friendship;
import main.model.entities.FriendshipStatus;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendsService {

    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;
    private final JWTUtil jwtUtil;
    private final PersonMapper personMapper;

    public FriendshipRs addFriend(String token, Long futureFriendId) {
        Person srcPerson = getPersonByToken(token);
        Optional<Person> optionalDstPerson = personsRepository.findPersonById(futureFriendId);
        if (!optionalDstPerson.isPresent()) {
            String descriptionError = "Person with ID" + futureFriendId + " doesn't exist";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = optionalDstPerson.get();
        modifyFriendShipStatus(srcPerson, dstPerson, FriendshipStatusTypes.FRIEND, FriendshipStatusTypes.FRIEND);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public FriendshipRs deleteFriend(String token, Long idDeletableFriend) {
        Person srcPerson = getPersonByToken(token);
        Optional<Person> optionalDstPerson = personsRepository.findPersonById(idDeletableFriend);
        if (!optionalDstPerson.isPresent()) {
            String descriptionError = "Person with ID" + idDeletableFriend + " isn't your friend";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = optionalDstPerson.get();
        modifyFriendShipStatus(srcPerson, dstPerson, FriendshipStatusTypes.REQUEST, FriendshipStatusTypes.SUBSCRIBED);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public CommonResponse<List<PersonResponse>> getFriends(String token, Integer page, Integer size) {
        List<PersonResponse> requestedPersons = getPersons(token, FriendshipStatusTypes.FRIEND, page, size);
        return buildCommonResponse(requestedPersons);
    }

    public FriendshipRs sendFriendshipRequest(String token, Long potentialFriendId) {
        Person srcPerson = getPersonByToken(token);
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(potentialFriendId);
        Person dstPerson = dstPersonOptional.get();
        FriendshipStatus srcFriendshipStatus = new FriendshipStatus(LocalDateTime.now(), FriendshipStatusTypes.SUBSCRIBED);
        FriendshipStatus dstFriendshipStatus = new FriendshipStatus(LocalDateTime.now(), FriendshipStatusTypes.REQUEST);
        friendshipStatusesRepository.save(srcFriendshipStatus);
        friendshipStatusesRepository.save(dstFriendshipStatus);
        Friendship srcFriendship = new Friendship(LocalDateTime.now(), srcPerson, dstPerson, srcFriendshipStatus);
        Friendship dstFriendship = new Friendship(LocalDateTime.now(), dstPerson, srcPerson, srcFriendshipStatus);
        friendshipsRepository.save(srcFriendship);
        friendshipsRepository.save(dstFriendship);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public FriendshipRs deleteSentFriendshipRequest(String token, Long requestedPersonId) {
        Person srcPerson = getPersonByToken(token);
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(requestedPersonId);
        Person dstPerson = dstPersonOptional.get();
        Friendship srcFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(srcPerson, dstPerson);
        Friendship dstFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(dstPerson, srcPerson);
        FriendshipStatus srcFriendshipStatus = friendshipStatusesRepository.
                findFriendshipStatusesById(srcFriendship.getFriendshipStatus().getId());
        FriendshipStatus dstFriendshipStatus = friendshipStatusesRepository.
                findFriendshipStatusesById(dstFriendship.getFriendshipStatus().getId());
        friendshipsRepository.delete(srcFriendship);
        friendshipsRepository.delete(dstFriendship);
        friendshipStatusesRepository.delete(srcFriendshipStatus);
        friendshipStatusesRepository.delete(dstFriendshipStatus);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public CommonResponse<List<PersonResponse>> getRequestedPersons(String token, Integer page, Integer size) {
        List<PersonResponse> requestedPersons = getPersons(token, FriendshipStatusTypes.REQUEST, page, size);
        return buildCommonResponse(requestedPersons);
    }

    private List<PersonResponse> getPersons(String token, FriendshipStatusTypes friendshipStatusTypes,
                                      Integer page, Integer size) {
        Person srcPerson = getPersonByToken(token);
        List<Friendship> srcPersonFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson).stream().
                filter(friendship -> friendship.getFriendshipStatus().getCode() == friendshipStatusTypes).
                collect(Collectors.toList());
        if (srcPersonFriendships.size() == 0) {
            return new ArrayList<>();
        } else {
            List<Person> persons = personsRepository.findPersonByDstFriendshipsIn(srcPersonFriendships);
            List<PersonResponse> responsePersons = personsToPersonResponses(persons);
            return responsePersons;
        }
    }

    private List<PersonResponse> personsToPersonResponses(List<Person> persons) {
        List<PersonResponse> personResponses = new ArrayList<>();
        for (Person person : persons) {
            personResponses.add(personMapper.toPersonResponse(person));
        }
        return personResponses;
    }

    private void modifyFriendShipStatus(Person srcPerson, Person dstPerson,
                                        FriendshipStatusTypes srcFriendshipStatusTypes,
                                        FriendshipStatusTypes dstFriendshipStatusTypes) {
        Friendship srcFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(srcPerson, dstPerson);
        Friendship dstFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(dstPerson, srcPerson);
        FriendshipStatus srcFriendshipStatus = friendshipStatusesRepository.
                findFriendshipStatusesById(srcFriendship.getFriendshipStatus().getId());
        FriendshipStatus dstFriendshipStatus = friendshipStatusesRepository.
                findFriendshipStatusesById(dstFriendship.getFriendshipStatus().getId());
        srcFriendshipStatus.setCode(srcFriendshipStatusTypes);
        srcFriendshipStatus.setTime(LocalDateTime.now());
        dstFriendshipStatus.setCode(dstFriendshipStatusTypes);
        dstFriendshipStatus.setTime(LocalDateTime.now());
        friendshipStatusesRepository.save(srcFriendshipStatus);
        friendshipStatusesRepository.save(dstFriendshipStatus);
    }

    private Person getPersonByToken(String token) {
        String personEmail = jwtUtil.extractUserName(token);
        Person person = personsRepository.findPersonByEmail(personEmail).orElseThrow();
        return person;
    }

    private CommonResponse<List<PersonResponse>> buildCommonResponse(List<PersonResponse> personResponses) {
        return CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .total((long) personResponses.size())
                .data(personResponses)
                .build();
    }
}