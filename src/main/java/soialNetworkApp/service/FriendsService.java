package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.FriendshipRs;
import soialNetworkApp.api.response.PersonRs;
import soialNetworkApp.errors.PersonException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.mappers.FriendMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
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
        //TODO: переделать на friendshipsRepository.findFriendshipBySrcPersonAndDstPerson()
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        Optional<Friendship> optionalSrcFriendship = getFriendshipByDstPerson(srcFriendships, dstPerson);
        FriendshipStatusTypes srcFriendshipStatusType = UNKNOWN;
        if (optionalSrcFriendship.isPresent()) {
            srcFriendshipStatusType = optionalSrcFriendship.get().getFriendshipStatus();
        }
        return srcFriendshipStatusType;
    }

    public void userBlocksUser(Long blockUserId) throws PersonException {
        Person me = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Optional<Person> dstPerson = personsRepository.findPersonById(blockUserId);
        if (dstPerson.isEmpty()) {
            throw new PersonException("User to block not found by id: " + blockUserId);
        }
        Friendship meSrcFriendship = friendshipsRepository.findFriendshipBySrcPersonIdAndDstPersonId(me.getId(), blockUserId);
        Friendship meDstFriendship = friendshipsRepository.findFriendshipBySrcPersonIdAndDstPersonId(blockUserId, me.getId());
        if (meSrcFriendship != null && meSrcFriendship.getFriendshipStatus().equals(BLOCKED)) {
            unblockPerson(meSrcFriendship);
        } else {
            blockPerson(meDstFriendship, meSrcFriendship, me, dstPerson.get());
        }
    }

    //TODO:переименовать в createFriendshipRequest или сделать метод общеиспользуемым добавив в параметры FriendshipsStatusTypes. Добавить возвращаемое значение, удалить сохранение из метода
    private void createFriendshipObjects(Person srcPerson, Person dstPerson) {
        Friendship srcFriendship = new Friendship(LocalDateTime.now(), srcPerson, dstPerson, REQUEST);
        Friendship dstFriendship = new Friendship(LocalDateTime.now(), dstPerson, srcPerson, RECEIVED_REQUEST);
        friendshipsRepository.save(srcFriendship);
        friendshipsRepository.save(dstFriendship);
        if (dstPerson.getPersonSettings() != null && dstPerson.getPersonSettings().getFriendRequestNotification()) {
            notificationsService.createNotification(dstFriendship, dstPerson);
        }
    }

    private void deleteFriendships(Person srcPerson, Person dstPerson) {
        //TODO: переделать на friendshipsRepository.findFriendshipBySrcPersonAndDstPerson()
        //TODO: косяк если в таблице только одна запись (user1 - user2 - FRIEND, а user2 - user1 - FRIEND не существует)
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
        //TODO: переделать на friendshipsRepository.findFriendshipBySrcPersonAndDstPerson()
        List<Friendship> srcFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson);
        Optional<Friendship> optionalSrcFriendship = getFriendshipByDstPerson(srcFriendships, dstPerson);
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
        List<PersonRs> personRespons = new ArrayList<>();
        for (Person person : persons) {
            personRespons.add(friendMapper.toFriendResponse(person, getStatusTwoPersons(person, srcPerson)));
        }
        return personRespons;
    }

    //TODO:удалить данный метод
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

    private void blockPerson(Friendship meDstFriendship, Friendship meSrcFriendship, Person me, Person dstPerson) {
        if (meDstFriendship != null) {
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
