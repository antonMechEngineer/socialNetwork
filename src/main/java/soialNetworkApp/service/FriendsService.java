package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.errors.FriendshipNotFoundException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.kafka.NotificationsKafkaProducer;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;

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
    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;
    private final NotificationsService notificationsService;
    private final NotificationsKafkaProducer notificationsKafkaProducer;

    public CommonRs<ComplexRs> addFriend(Long receivedFriendId) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(receivedFriendId);
        setFriendStatus(srcPerson, dstPerson);
        setFriendStatus(dstPerson, srcPerson);
        return CommonRs.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    public CommonRs<ComplexRs> sendFriendshipRequest(Long requestedFriendId) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(requestedFriendId);
        createFriendshipRequest(srcPerson, dstPerson);
        return CommonRs.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    public CommonRs<ComplexRs> deleteFriend(Long idDeletableFriend) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(idDeletableFriend);
        deleteFriendships(srcPerson, dstPerson);
        return CommonRs.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    public CommonRs<ComplexRs> deleteSentFriendshipRequest(Long idRequestedFriend) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(idRequestedFriend);
        deleteFriendships(srcPerson, dstPerson);
        return CommonRs.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    public CommonRs<List<PersonRs>> getFriends(Integer offset, Integer size) throws Exception {
        Person srcPerson = getSrcPerson();
        Page<Person> requestedPersons = getPersons(srcPerson, offset, size, FriendshipStatusTypes.FRIEND);
        return buildCommonResponse(requestedPersons, srcPerson, offset);
    }

    public CommonRs<List<PersonRs>> getRequestedPersons(Integer offset, Integer size) throws Exception {
        Person srcPerson = getSrcPerson();
        Page<Person> personsSentResponse = getPersons(srcPerson, offset, size, RECEIVED_REQUEST);
        return buildCommonResponse(personsSentResponse, srcPerson, offset);
    }

    public FriendshipStatusTypes getStatusTwoPersons(Person dstPerson, Person srcPerson) {
        Optional<Friendship> optionalSrcFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(srcPerson, dstPerson);
        FriendshipStatusTypes srcFriendshipStatusType = UNKNOWN;
        if (optionalSrcFriendship.isPresent()) {
            srcFriendshipStatusType = optionalSrcFriendship.get().getFriendshipStatus();
        }
        return srcFriendshipStatusType;
    }

    public void userBlocksUser(Long blockId) throws Exception {
        Person srcPerson = getSrcPerson();
        Person dstPerson = getDstPerson(blockId);
        Optional <Friendship> meOptionalSrcFriendship = friendshipsRepository.findFriendshipBySrcPersonIdAndDstPersonId(srcPerson.getId(), blockId);
        Friendship meDstFriendship = friendshipsRepository.findFriendshipBySrcPersonIdAndDstPersonId(blockId, srcPerson.getId()).orElseThrow(new FriendshipNotFoundException(dstPerson, srcPerson));
        if(meOptionalSrcFriendship/)

        if (meSrcFriendship.getFriendshipStatus().equals(BLOCKED)) {
            unblockPerson(meSrcFriendship);
        } else {
            blockPerson(meDstFriendship, meSrcFriendship, srcPerson, dstPerson);
        }
    }

    public CommonRs<List<PersonRs>> getOutgoingRequests(Integer offset, Integer size) throws Exception {
        Person srcPerson = getSrcPerson();
        Page<Person> requestedPersons = getPersons(srcPerson, offset, size, REQUEST);
        return buildCommonResponse(requestedPersons, srcPerson, offset);
    }

    private void createFriendshipRequest(Person srcPerson, Person dstPerson) {
        Friendship srcFriendship = new Friendship(LocalDateTime.now(), srcPerson, dstPerson, REQUEST);
        Friendship dstFriendship = new Friendship(LocalDateTime.now(), dstPerson, srcPerson, RECEIVED_REQUEST);
        friendshipsRepository.save(srcFriendship);
        friendshipsRepository.save(dstFriendship);
        if (dstPerson.getPersonSettings() != null && dstPerson.getPersonSettings().getFriendRequestNotification()) {
            notificationsKafkaProducer.sendMessage(dstFriendship, dstPerson);
            notificationsService.sendNotificationToTelegramBot(dstFriendship, dstPerson);
        }
    }

    private void deleteFriendships(Person srcPerson, Person dstPerson) throws Exception {
        Friendship srcFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(srcPerson, dstPerson).orElseThrow(new FriendshipNotFoundException(srcPerson, dstPerson));
        Friendship dstFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(dstPerson, srcPerson).orElseThrow(new FriendshipNotFoundException(dstPerson, srcPerson));
        notificationsService.deleteNotification(srcFriendship);
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

    private void setFriendStatus(Person srcPerson, Person dstPerson) {
        Optional<Friendship> optionalSrcFriendship = friendshipsRepository.findFriendshipBySrcPersonAndDstPerson(srcPerson, dstPerson);
        if (optionalSrcFriendship.isPresent()) {
            Friendship friendship = optionalSrcFriendship.get();
            friendship.setFriendshipStatus(FRIEND);
            friendship.setSentTime(LocalDateTime.now());
            friendshipsRepository.save(friendship);
        }
    }

    private List<Friendship> getFriendshipsByType(List<Friendship> friendships, FriendshipStatusTypes friendshipStatusTypes) {
        return friendships.stream().filter(friendship -> friendship.getFriendshipStatus() == friendshipStatusTypes).
                collect(Collectors.toList());
    }

    private CommonRs<List<PersonRs>> buildCommonResponse(Page<Person> persons, Person srcPerson, Integer offset) {
        List<PersonRs> responsePersons = personsToPersonResponses(persons.getContent(), srcPerson);
        return CommonRs.<List<PersonRs>>builder()
                .timestamp(System.currentTimeMillis())
                .offset(offset)
                .perPage(persons.getSize())
                .total(persons.getTotalElements())
                .data(responsePersons)
                .build();
    }

    private List<PersonRs> personsToPersonResponses(List<Person> persons, Person srcPerson) {
        List<PersonRs> personResponse = new ArrayList<>();
        for (Person person : persons) {
            person.setFriendStatus(getStatusTwoPersons(person, srcPerson));
            personResponse.add(personMapper.toPersonResponse(person));
        }
        return personResponse;
    }

    private Person getSrcPerson() throws Exception {
        String srcEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return personsRepository.findPersonByEmail(srcEmail)
                .orElseThrow(new PersonNotFoundException(srcEmail));
    }

    private Person getDstPerson(Long id) throws Exception {
        return personsRepository.findPersonById(id).orElseThrow(new PersonNotFoundException(id));
    }

    private void blockPerson(Friendship meDstFriendship, Friendship meSrcFriendship, Person me, Person dstPerson) {
        if (meDstFriendship != null && meDstFriendship.getFriendshipStatus() != BLOCKED) {
            friendshipsRepository.delete(meDstFriendship);
        }
        if (meSrcFriendship != null) {
            meSrcFriendship.setFriendshipStatus(BLOCKED);
            meSrcFriendship.setSentTime(LocalDateTime.now());
        } else {
            meSrcFriendship = new Friendship(LocalDateTime.now(), me, dstPerson, BLOCKED);
        }
        friendshipsRepository.save(meSrcFriendship);
    }

    private void unblockPerson(Friendship meSrcFriendship) {
        friendshipsRepository.delete(meSrcFriendship);
    }
}
