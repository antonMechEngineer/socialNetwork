package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.FriendshipRs;
import main.api.response.PersonResponse;
import main.mappers.FriendMapper;
import main.model.entities.Friendship;
import main.model.entities.FriendshipStatus;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipStatusesRepository;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import main.security.jwt.JWTUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final FriendMapper friendMapper;
    private final String defaultError = "ok";

    public FriendshipRs addFriend(String token, Long futureFriendId) {
        Person srcPerson = getSrcPersonByToken(token);
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(futureFriendId);
        if (!dstPersonOptional.isPresent()) {
           String descriptionError = "Person with ID" + futureFriendId + " doesn't exist";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = dstPersonOptional.get();
        modifyFriendShipStatus(srcPerson, dstPerson);
        modifyFriendShipStatus(dstPerson, srcPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public FriendshipRs sendFriendshipRequest(String token, Long potentialFriendId) {
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(potentialFriendId);
        if (!dstPersonOptional.isPresent()) {
           String descriptionError = "Person with ID" + potentialFriendId + " doesn't exist";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = dstPersonOptional.get();
        createFriendshipObjects(getSrcPersonByToken(token), dstPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public FriendshipRs deleteFriend(String token, Long idDeletableFriend) {
        Optional<Person> optionalDstPerson = personsRepository.findPersonById(idDeletableFriend);
        if (!optionalDstPerson.isPresent()) {
           String descriptionError = "Person with ID" + idDeletableFriend + " isn't your friend";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = optionalDstPerson.get();
        deleteFriendships(getSrcPersonByToken(token), dstPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public FriendshipRs deleteSentFriendshipRequest(String token, Long idRequestedPerson) {
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(idRequestedPerson);
        if (!dstPersonOptional.isPresent()) {
            String descriptionError = "Person with ID" + idRequestedPerson + " isn't requested";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = dstPersonOptional.get();
        deleteFriendships(getSrcPersonByToken(token), dstPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public CommonResponse<List<PersonResponse>> getFriends(String token) {
        List<PersonResponse> requestedPersons = getPersons(token, FriendshipStatusTypes.FRIEND);
        CommonResponse<List<PersonResponse>> commonResponse = buildCommonResponse(requestedPersons);
        return commonResponse;
    }

    public CommonResponse<List<PersonResponse>> getRequestedPersons(String token) {
        List<PersonResponse> personsSentResponse = getPersons(token, FriendshipStatusTypes.RECEIVED_REQUEST);
        return buildCommonResponse(personsSentResponse);
    }

    public Person getSrcPersonByToken(String token) {
        String personEmail = jwtUtil.extractUserName(token);
        Person person = personsRepository.findPersonByEmail(personEmail).orElseThrow();
        return person;
    }

    public FriendshipStatusTypes getStatusTwoPersons(Person dstPerson, Person srcPerson){
        Optional<Friendship> optionalSrcFriendship = friendshipsRepository.findFriendshipBySrcPerson(srcPerson).
                stream().filter(fs -> fs.getDstPerson() == dstPerson).collect(Collectors.toList()).stream().findFirst();
        FriendshipStatusTypes srcFriendshipStatusType = FriendshipStatusTypes.UNKNOWN;
        if (optionalSrcFriendship.isPresent()) {
            srcFriendshipStatusType = optionalSrcFriendship.get().getFriendshipStatus().getCode();
        }
        return srcFriendshipStatusType;
    }

    private void createFriendshipObjects(Person srcPerson, Person dstPerson) {
        FriendshipStatus srcFriendshipStatus = new FriendshipStatus(LocalDateTime.now(),
                FriendshipStatusTypes.REQUEST, FriendshipStatusTypes.REQUEST.toString());
        FriendshipStatus dstFriendshipStatus = new FriendshipStatus(LocalDateTime.now(),
                FriendshipStatusTypes.RECEIVED_REQUEST, FriendshipStatusTypes.RECEIVED_REQUEST.toString());
        friendshipStatusesRepository.save(srcFriendshipStatus);
        friendshipStatusesRepository.save(dstFriendshipStatus);
        Friendship srcFriendship = new Friendship(LocalDateTime.now(), srcPerson, dstPerson, srcFriendshipStatus);
        Friendship dstFriendship = new Friendship(LocalDateTime.now(), dstPerson, srcPerson, dstFriendshipStatus);
        friendshipsRepository.save(srcFriendship);
        friendshipsRepository.save(dstFriendship);
    }

    private void deleteFriendships(Person srcPerson, Person dstPerson) {
        Friendship srcFriendship = getFriendshipByDstPerson(friendshipsRepository.findFriendshipBySrcPerson(srcPerson), dstPerson);
        Friendship dstFriendship = getFriendshipByDstPerson(friendshipsRepository.findFriendshipBySrcPerson(dstPerson), srcPerson);
        friendshipsRepository.delete(srcFriendship);
        friendshipsRepository.delete(dstFriendship);
    }

    private List<PersonResponse> getPersons(String token, FriendshipStatusTypes friendshipStatusTypes) {
        Person srcPerson = getSrcPersonByToken(token);
        List<Friendship> srcPersonFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        srcPersonFriendships = getFriendshipsByType(srcPersonFriendships, friendshipStatusTypes);
        if (srcPersonFriendships.size() == 0) {
            return new ArrayList<>();
        } else {
            List<Person> persons = srcPersonFriendships.stream().map(Friendship::getDstPerson).collect(Collectors.toList());
            List<PersonResponse> responsePersons = personsToPersonResponses(persons, srcPerson);
            return responsePersons;
        }
    }

    private void modifyFriendShipStatus(Person srcPerson, Person dstPerson) {

        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        Optional<Friendship> optionalSrcFriendship = srcFriendships.stream().
                filter(friendship -> friendship.getDstPerson().equals(dstPerson)).findFirst();
        if (optionalSrcFriendship.isPresent()) {
            FriendshipStatus srcFriendshipStatus = optionalSrcFriendship.get().getFriendshipStatus();
            srcFriendshipStatus.setCode(FriendshipStatusTypes.FRIEND);
            srcFriendshipStatus.setName(FriendshipStatusTypes.FRIEND.toString());
            srcFriendshipStatus.setTime(LocalDateTime.now());
            friendshipStatusesRepository.save(srcFriendshipStatus);
        }
    }

    private List<Friendship> getFriendshipsByType(List<Friendship> friendships, FriendshipStatusTypes friendshipStatusTypes) {
        return friendships.stream().filter(friendship -> friendship.getFriendshipStatus().getCode() == friendshipStatusTypes).
                collect(Collectors.toList());
    }

    private CommonResponse<List<PersonResponse>> buildCommonResponse(List<PersonResponse> personResponses) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<PersonResponse> pagePersons = new PageImpl<>(personResponses, pageable, personResponses.size());
        return CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .offset(pagePersons.getSize())
                .perPage(8)
                .total((long) personResponses.size())
                .data(personResponses)
                .build();
    }

    private List<PersonResponse> personsToPersonResponses(List<Person> persons, Person srcPerson) {
        List<PersonResponse> personResponses = new ArrayList<>();
        for (Person person : persons) {
            personResponses.add(friendMapper.toFriendResponse(person, getStatusTwoPersons(person, srcPerson)));
        }
        return personResponses;
    }

    private Friendship getFriendshipByDstPerson(List<Friendship> friendships, Person dstPerson) {
        return friendships.stream().filter(fs -> fs.getDstPerson() == dstPerson).collect(Collectors.toList()).get(0);
    }

}
