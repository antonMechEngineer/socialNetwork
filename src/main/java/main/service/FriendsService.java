package main.service;
import lombok.RequiredArgsConstructor;
import main.api.response.ComplexRs;
import main.api.response.FriendshipRs;
import main.api.response.ListResponseRsPersonRs;
import main.api.response.PersonRs;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendsService {

    //локальный: docker run -d -p 8080:80 -t chuchos/javapro_teams27_frontend_localhost
    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipStatusesRepository friendshipStatusesRepository;
    private final PersonsRepository personsRepository;
    private final JWTUtil jwtUtil;

    public FriendshipRs addFriend(String token, Long futureFriendId){
        Person srcPerson = getPersonByToken(token);
        Optional<Person> optionalDstPerson = personsRepository.findPersonById(futureFriendId);
        if (!optionalDstPerson.isPresent()){
            String descriptionError = "Person with ID" + futureFriendId + " doesn't exist";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = optionalDstPerson.get();
        modifyFriendShipStatus(srcPerson, dstPerson, FriendshipStatusTypes.FRIEND, FriendshipStatusTypes.FRIEND);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public FriendshipRs deleteFriend(String token, Long idDeletableFriend){
        Person srcPerson = getPersonByToken(token);
        Optional<Person> optionalDstPerson = personsRepository.findPersonById(idDeletableFriend);
        if (!optionalDstPerson.isPresent()){
            String descriptionError = "Person with ID" + idDeletableFriend + " isn't your friend";
            return new FriendshipRs(descriptionError, LocalDateTime.now().toString(), new ComplexRs(descriptionError));
        }
        Person dstPerson = optionalDstPerson.get();
        modifyFriendShipStatus(srcPerson, dstPerson, FriendshipStatusTypes.REQUEST, FriendshipStatusTypes.SUBSCRIBED);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public ListResponseRsPersonRs getFriends(String token, Integer page, Integer size){
        Page<Person> friends = getPagePersons(token, FriendshipStatusTypes.FRIEND, page, size);
        return new ListResponseRsPersonRs("ok",
                System.currentTimeMillis(),
                Long.valueOf(friends.getTotalElements()).intValue(),
                buildPersonRs(friends.getContent()),
                friends.getNumberOfElements(),
                "");
    }

    public FriendshipRs sendFriendshipRequest(String token, Long potentialFriendId){
        Person srcPerson = getPersonByToken(token);
        Optional <Person> dstPersonOptional = personsRepository.findPersonById(potentialFriendId);
        Person dstPerson = dstPersonOptional.get();
        FriendshipStatus srcFriendshipStatus = new FriendshipStatus(LocalDateTime.now(), FriendshipStatusTypes.SUBSCRIBED);
        FriendshipStatus dstFriendshipStatus = new FriendshipStatus(LocalDateTime.now(), FriendshipStatusTypes.REQUEST);
        friendshipStatusesRepository.save(srcFriendshipStatus);
        friendshipStatusesRepository.save(dstFriendshipStatus);
        Friendship srcFriendship = new Friendship(srcFriendshipStatus, LocalDateTime.now(), srcPerson, dstPerson);
        Friendship dstFriendship = new Friendship(dstFriendshipStatus, LocalDateTime.now(), dstPerson, srcPerson);
        friendshipsRepository.save(srcFriendship);
        friendshipsRepository.save(dstFriendship);
        return new FriendshipRs("ok", LocalDateTime.now().toString(), new ComplexRs("ok"));
    }

    public FriendshipRs deleteSentFriendshipRequest(String token, Long requestedPersonId){
        Person srcPerson = getPersonByToken(token);
        Optional <Person> dstPersonOptional = personsRepository.findPersonById(requestedPersonId);
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

    public ListResponseRsPersonRs getRequestedPersons(String token, Integer page, Integer size){
        Page<Person> requestedPersons = getPagePersons(token, FriendshipStatusTypes.REQUEST, page, size);
        return new ListResponseRsPersonRs("ok",
                System.currentTimeMillis(),
                Long.valueOf(requestedPersons.getTotalElements()).intValue(),
                buildPersonRs(requestedPersons.getContent()),
                requestedPersons.getNumberOfElements(),
                "");
    }

    private Page<Person> getPagePersons(String token, FriendshipStatusTypes friendshipStatusTypes,
                                        Integer page, Integer size){
        Person srcPerson = getPersonByToken(token);
        List<Friendship> srcPersonFriendships = friendshipsRepository.findFriendshipBySrcPerson(srcPerson).stream().
                filter(friendship -> friendship.getFriendshipStatus().getCode() == friendshipStatusTypes).
                collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size, Sort.by("last_name").descending());
        Page<Person> persons = personsRepository.findPersonByDstFriendshipsIn(srcPersonFriendships, pageable);
        return persons;
    }

    private List<PersonRs> buildPersonRs(List<Person> persons){
        return persons.stream().map(PersonRs::new).collect(Collectors.toList());
    }

    private void modifyFriendShipStatus(Person srcPerson, Person dstPerson,
                                        FriendshipStatusTypes srcFriendshipStatusTypes,
                                        FriendshipStatusTypes dstFriendshipStatusTypes){
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

    private Person getPersonByToken(String token){
        String personEmail = jwtUtil.extractUserName(token);
        Person person = personsRepository.findPersonByEmail(personEmail).orElseThrow();
        return person;
    }
}

