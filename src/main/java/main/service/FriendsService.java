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

    // TODO: 25.11.2022 не забудь начать делать тестирование
    // TODO: 25.11.2022 в каждом методе можно сделать общий errorDescription и при возврате в нужный момент его переопределять
    public FriendshipRs addFriend(String token, Long futureFriendId) {
        System.out.println("Invoked by friendservice.addFriend");
        Person srcPerson = getPersonByToken(token);
        Optional<Person> optionalDstPerson = personsRepository.findPersonById(futureFriendId);
        if (!optionalDstPerson.isPresent()) {
            String descriptionError = "Person with ID" + futureFriendId + " doesn't exist";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = optionalDstPerson.get();
        modifyFriendShipStatus(srcPerson, dstPerson, FriendshipStatusTypes.FRIEND);  // TODO: 25.11.2022 метод можно сделать не void a String, где по дефолту message ok, а в случае ошибки description error
        modifyFriendShipStatus(dstPerson, srcPerson, FriendshipStatusTypes.FRIEND);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public FriendshipRs sendFriendshipRequest(String token, Long potentialFriendId) {
        System.out.println("Invoked by friendservice.sendFriendshipRequest");
        Person srcPerson = getPersonByToken(token);
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(potentialFriendId);
        Person dstPerson = dstPersonOptional.get();
        createFriendshipObjects(srcPerson, dstPerson, FriendshipStatusTypes.REQUEST);
        createFriendshipObjects(srcPerson, dstPerson, FriendshipStatusTypes.REQUEST);
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
        deleteFriendships(srcPerson, dstPerson);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public FriendshipRs deleteSentFriendshipRequest(String token, Long idRequestedPerson) {
        Person srcPerson = getPersonByToken(token);
        Optional<Person> dstPersonOptional = personsRepository.findPersonById(idRequestedPerson);
        if (!dstPersonOptional.isPresent()) {
            String descriptionError = "Person with ID" + idRequestedPerson + " isn't requested";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = dstPersonOptional.get();
        deleteFriendships(srcPerson, dstPerson);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public CommonResponse<List<PersonResponse>> getFriends(String token) {
        List<PersonResponse> requestedPersons = getPersons(token, FriendshipStatusTypes.FRIEND);
        CommonResponse<List<PersonResponse>> commonResponse = buildCommonResponse(requestedPersons);
        return commonResponse;
    }

    private void createFriendshipObjects(Person srcPerson, Person dstPerson, FriendshipStatusTypes friendshipStatusType) {
        FriendshipStatus friendshipStatus = new FriendshipStatus(LocalDateTime.now(), friendshipStatusType);
        friendshipStatusesRepository.save(friendshipStatus);
        Friendship friendship = new Friendship(LocalDateTime.now(), srcPerson, dstPerson, friendshipStatus);
        friendshipsRepository.save(friendship);
    }

    private void deleteFriendships(Person srcPerson, Person dstPerson) {
        Friendship srcFriendship = getFriendshipByDstPerson(friendshipsRepository.findFriendshipBySrcPerson(srcPerson), dstPerson);
        Friendship dstFriendship = getFriendshipByDstPerson(friendshipsRepository.findFriendshipBySrcPerson(dstPerson), srcPerson);
        friendshipsRepository.delete(srcFriendship);
        friendshipsRepository.delete(dstFriendship);
    }

    public CommonResponse<List<PersonResponse>> getRequestedPersons(String token) {
        List<PersonResponse> requestedPersons = getPersons(token, FriendshipStatusTypes.REQUEST);
        return buildCommonResponse(requestedPersons);
    }

    private List<PersonResponse> getPersons(String token, FriendshipStatusTypes friendshipStatusTypes) {
        Person srcPerson = getPersonByToken(token);
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

    private void modifyFriendShipStatus(Person srcPerson, Person dstPerson, FriendshipStatusTypes srcFriendshipStatusTypes) {
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        Optional<Friendship> optionalSrcFriendship = getFriendshipsByType(srcFriendships, srcFriendshipStatusTypes).stream().
                filter(friendship -> friendship.getDstPerson().equals(dstPerson)).findFirst();
        if (optionalSrcFriendship.isPresent()) {
            FriendshipStatus srcFriendshipStatus = optionalSrcFriendship.get().getFriendshipStatus();
            srcFriendshipStatus.setCode(srcFriendshipStatusTypes);
            srcFriendshipStatus.setTime(LocalDateTime.now());
            friendshipStatusesRepository.save(srcFriendshipStatus);
        }
    }

    private List<Friendship> getFriendshipsByType(List<Friendship> friendships, FriendshipStatusTypes friendshipStatusTypes) {
        return friendships.stream().filter(friendship -> friendship.getFriendshipStatus().getCode() == friendshipStatusTypes).
                collect(Collectors.toList());
    }

    public Person getPersonByToken(String token) {
        String personEmail = jwtUtil.extractUserName(token);
        Person person = personsRepository.findPersonByEmail(personEmail).orElseThrow();
        return person;
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
            Optional<Friendship> optionalSrcFriendship = friendshipsRepository.findFriendshipBySrcPerson(srcPerson).
                    stream().filter(fs -> fs.getDstPerson() == person).collect(Collectors.toList()).stream().findFirst();
            FriendshipStatusTypes srcFriendshipStatusType = FriendshipStatusTypes.UNKNOWN;
            if (optionalSrcFriendship.isPresent()) {
                srcFriendshipStatusType = optionalSrcFriendship.get().getFriendshipStatus().getCode();
            }
            personResponses.add(friendMapper.toFriendResponse(person, srcFriendshipStatusType));
        }
        return personResponses;
    }

    private Friendship getFriendshipByDstPerson(List<Friendship> friendships, Person dstPerson) {
        return friendships.stream().filter(fs -> fs.getDstPerson() == dstPerson).collect(Collectors.toList()).get(0);
    }


}
