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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static main.model.enums.FriendshipStatusTypes.*;


@RequiredArgsConstructor
@Service
public class FriendsService {

    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;
    private final FriendMapper friendMapper;
    private final NotificationsService notificationsService;
    private final String defaultError = "ok";

    public FriendshipRs addFriend(Long receivedFriendId) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Person dstPerson = personsRepository.findPersonById(receivedFriendId).orElseThrow();
        modifyFriendShipStatus(srcPerson, dstPerson);
        modifyFriendShipStatus(dstPerson, srcPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public FriendshipRs sendFriendshipRequest(Long requestedFriendId) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Person dstPerson = personsRepository.findPersonById(requestedFriendId).orElseThrow();
        createFriendshipObjects(srcPerson, dstPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public FriendshipRs deleteFriend(Long idDeletableFriend) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Person dstPerson = personsRepository.findPersonById(idDeletableFriend).orElseThrow();
        deleteFriendships(srcPerson, dstPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public FriendshipRs deleteSentFriendshipRequest(Long idRequestedFriend) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Person dstPerson = personsRepository.findPersonById(idRequestedFriend).orElseThrow();
        deleteFriendships(srcPerson, dstPerson);
        return new FriendshipRs(defaultError, LocalDateTime.now().toString(), new ComplexRs(defaultError));
    }

    public CommonResponse<List<PersonResponse>> getFriends(Integer offset, Integer size) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Page<Person> requestedPersons = getPersons(srcPerson, offset, size, FriendshipStatusTypes.FRIEND);
        return buildCommonResponse(requestedPersons, srcPerson, offset);
    }

    public CommonResponse<List<PersonResponse>> getRequestedPersons(Integer offset, Integer size) {
        Person srcPerson = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        Page<Person> personsSentResponse = getPersons(srcPerson, offset, size, RECEIVED_REQUEST);
        return buildCommonResponse(personsSentResponse, srcPerson, offset);
    }

    public FriendshipStatusTypes getStatusTwoPersons(Person dstPerson, Person srcPerson) {
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        Optional<Friendship> optionalSrcFriendship = getFriendshipByDstPerson(srcFriendships, dstPerson);
        FriendshipStatusTypes srcFriendshipStatusType = UNKNOWN;
        if (optionalSrcFriendship.isPresent()) {
            srcFriendshipStatusType = optionalSrcFriendship.get().getFriendshipStatus().getCode();
        }
        return srcFriendshipStatusType;
    }

    private void createFriendshipObjects(Person srcPerson, Person dstPerson) {
        FriendshipStatus srcFriendshipStatus = new FriendshipStatus(LocalDateTime.now(), REQUEST, REQUEST.toString());
        FriendshipStatus dstFriendshipStatus = new FriendshipStatus(LocalDateTime.now(), RECEIVED_REQUEST, RECEIVED_REQUEST.toString());
        friendshipStatusesRepository.save(srcFriendshipStatus);
        friendshipStatusesRepository.save(dstFriendshipStatus);
        Friendship srcFriendship = new Friendship(LocalDateTime.now(), srcPerson, dstPerson, srcFriendshipStatus);
        Friendship dstFriendship = new Friendship(LocalDateTime.now(), dstPerson, srcPerson, dstFriendshipStatus);
        friendshipsRepository.save(srcFriendship);
        friendshipsRepository.save(dstFriendship);
        notificationsService.createNotification(srcFriendship, dstPerson);
        notificationsService.createNotification(dstFriendship, srcPerson);
    }

    private void deleteFriendships(Person srcPerson, Person dstPerson) {
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        List<Friendship> dstFriendships = friendshipsRepository.findFriendshipBySrcPerson(dstPerson);
        Friendship srcFriendship = getFriendshipByDstPerson(srcFriendships, dstPerson).orElseThrow();
        Friendship dstFriendship = getFriendshipByDstPerson(dstFriendships, srcPerson).orElseThrow();
        friendshipsRepository.delete(srcFriendship);
        friendshipsRepository.delete(dstFriendship);
    }

    private Page<Person> getPersons(Person srcPerson, Integer offset, Integer size, FriendshipStatusTypes friendshipStatusTypes) {
        Pageable pageable = PageRequest.of(offset, size);
        List<Friendship> srcPersonFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        srcPersonFriendships = getFriendshipsByType(srcPersonFriendships, friendshipStatusTypes);
        List<Long> friendlyIds = srcPersonFriendships.stream().map(Friendship::getDstPerson).map(Person::getId).collect(Collectors.toList());
        Page<Person> persons = personsRepository.findPersonByIdIn(friendlyIds, pageable);
        return persons;
    }

    private void modifyFriendShipStatus(Person srcPerson, Person dstPerson) {
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        Optional<Friendship> optionalSrcFriendship = getFriendshipByDstPerson(srcFriendships, dstPerson);
        if (optionalSrcFriendship.isPresent()) {
            FriendshipStatus srcFriendshipStatus = optionalSrcFriendship.get().getFriendshipStatus();
            srcFriendshipStatus.setCode(FRIEND);
            srcFriendshipStatus.setName(FRIEND.toString());
            srcFriendshipStatus.setTime(LocalDateTime.now());
            friendshipStatusesRepository.save(srcFriendshipStatus);
            notificationsService.createNotification(optionalSrcFriendship.get(), dstPerson);
        }
    }

    private List<Friendship> getFriendshipsByType(List<Friendship> friendships, FriendshipStatusTypes friendshipStatusTypes) {
        return friendships.stream().filter(friendship -> friendship.getFriendshipStatus().getCode() == friendshipStatusTypes).
                collect(Collectors.toList());
    }

    private CommonResponse<List<PersonResponse>> buildCommonResponse(Page<Person> persons, Person srcPerson, Integer offset) {
        List<PersonResponse> responsePersons = personsToPersonResponses(persons.getContent(), srcPerson);
        return CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .offset(offset)
                .perPage(persons.getSize())
                .total(persons.getTotalElements())
                .data(responsePersons)
                .build();
    }


    private List<PersonResponse> personsToPersonResponses(List<Person> persons, Person srcPerson) {
        List<PersonResponse> personResponses = new ArrayList<>();
        for (Person person : persons) {
            personResponses.add(friendMapper.toFriendResponse(person, getStatusTwoPersons(person, srcPerson)));
        }
        return personResponses;
    }

    private Optional <Friendship> getFriendshipByDstPerson(List<Friendship> friendships, Person dstPerson) {
        return friendships.stream().filter(fs -> fs.getDstPerson() == dstPerson).findFirst();
    }

}
