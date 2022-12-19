package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.FriendshipRs;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.mappers.FriendMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.FriendshipStatus;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipStatusesRepository;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;
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

import static soialNetworkApp.model.enums.FriendshipStatusTypes.*;

@RequiredArgsConstructor
@Service
public class FriendsService {
    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;
    private final FriendMapper friendMapper;
    private final NotificationsService notificationsService;

    public FriendshipRs addFriend(Long receivedFriendId) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(receivedFriendId);
        modifyFriendShipStatus(srcPerson, dstPerson);
        modifyFriendShipStatus(dstPerson, srcPerson);
        return new FriendshipRs(LocalDateTime.now().toString(), ComplexRs.builder().build());
    }

    public FriendshipRs sendFriendshipRequest(Long requestedFriendId) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(requestedFriendId);
        createFriendshipObjects(srcPerson, dstPerson);
        return new FriendshipRs(LocalDateTime.now().toString(), ComplexRs.builder().build());
    }

    public FriendshipRs deleteFriend(Long idDeletableFriend) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(idDeletableFriend);
        deleteFriendships(srcPerson, dstPerson);
        return new FriendshipRs(LocalDateTime.now().toString(), ComplexRs.builder().build());
    }

    public FriendshipRs deleteSentFriendshipRequest(Long idRequestedFriend) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(idRequestedFriend);
        deleteFriendships(srcPerson, dstPerson);
        return new FriendshipRs(LocalDateTime.now().toString(), ComplexRs.builder().build());
    }

    public CommonResponse<List<PersonResponse>> getFriends(Integer offset, Integer size) throws Exception {
        Person srcPerson = getSrcPerson();
        Page<Person> requestedPersons = getPersons(srcPerson, offset, size, FriendshipStatusTypes.FRIEND);
        return buildCommonResponse(requestedPersons, srcPerson, offset);
    }

    public CommonResponse<List<PersonResponse>> getRequestedPersons(Integer offset, Integer size) throws Exception {
        Person srcPerson = getSrcPerson();
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
        if (dstPerson.getPersonSettings() != null && dstPerson.getPersonSettings().getFriendRequestNotification()) {
            notificationsService.createNotification(dstFriendship, dstPerson);
        }
    }

    private void deleteFriendships(Person srcPerson, Person dstPerson) {
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        List<Friendship> dstFriendships = friendshipsRepository.findFriendshipBySrcPerson(dstPerson);
        Friendship srcFriendship = getFriendshipByDstPerson(srcFriendships, dstPerson).orElseThrow();
        Friendship dstFriendship = getFriendshipByDstPerson(dstFriendships, srcPerson).orElseThrow();
        notificationsService.deleteNotification(dstFriendship);
        friendshipsRepository.delete(srcFriendship);
        friendshipsRepository.delete(dstFriendship);
    }

    private Page<Person> getPersons(Person srcPerson, Integer offset, Integer size, FriendshipStatusTypes friendshipStatusTypes) {
        Pageable pageable = PageRequest.of(offset, size);
        List<Friendship> srcPersonFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        srcPersonFriendships = getFriendshipsByType(srcPersonFriendships, friendshipStatusTypes);
        List<Long> friendlyIds = srcPersonFriendships.stream().map(Friendship::getDstPerson).map(Person::getId).collect(Collectors.toList());
        return personsRepository.findPersonByIdIn(friendlyIds, pageable);
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

    private Optional<Friendship> getFriendshipByDstPerson(List<Friendship> friendships, Person dstPerson) {
        return friendships.stream().filter(fs -> fs.getDstPerson() == dstPerson).findFirst();
    }

    private Person getSrcPerson() throws Exception {
        String srcEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return personsRepository.findPersonByEmail(srcEmail)
                .orElseThrow(new PersonNotFoundException("User by email: '" + srcEmail + "' not found"));
    }

    private Person getDstPerson(Long id) throws Exception {
        return personsRepository.findPersonById(id).orElseThrow(
                new PersonNotFoundException("User by id: '" + id + "' not found"));
    }
}
