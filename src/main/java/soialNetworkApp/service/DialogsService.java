package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.request.DialogUserShortListDto;
import soialNetworkApp.kafka.MessagesKafkaProducer;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.ComplexRs;
import soialNetworkApp.api.response.DialogRs;
import soialNetworkApp.api.response.MessageRs;
import soialNetworkApp.mappers.DialogMapper;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.util.CurrentUserExtractor;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogsService {

    private final FriendshipsRepository friendshipsRepository;
    private final DialogsRepository dialogsRepository;
    private final MessagesRepository messagesRepository;
    private final PersonsRepository personsRepository;
    private final DialogMapper dialogMapper;
    private final DialogMapService dialogMapService;
    private final CurrentUserExtractor currentUserExtractor;
    private final PersonMapper personMapper;
    private final MessagesKafkaProducer messagesKafkaProducer;


    public CommonRs<ComplexRs> getUnreadMessages() {
        return new CommonRs<>(new ComplexRs(messagesRepository
                .findAllByRecipientAndIsDeletedFalse(currentUserExtractor.getPerson())
                .stream()
                .filter(m -> m.getReadStatus().equals(ReadStatusTypes.SENT))
                .count()));
    }

    public CommonRs<ComplexRs> setReadMessages(Long dialogId) {
        final Long[] readCount = {0L};
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .stream()
                .filter(m -> m.getRecipient().equals(currentUserExtractor.getPerson()))
                .filter(m -> m.getReadStatus().equals(ReadStatusTypes.SENT))
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesKafkaProducer.sendMessage(m);
                    readCount[0]++;
                });
        return new CommonRs<>(new ComplexRs(readCount[0]));
    }

    public CommonRs<ComplexRs> beginDialog(DialogUserShortListDto dialogUserShortListDto) {
        Person currentPerson = currentUserExtractor.getPerson();
        Person anotherPerson = personsRepository.findPersonById(dialogUserShortListDto.getUserIds().get(0)).orElseThrow();
        Dialog dialog = (dialogsRepository.findDialogByFirstPersonAndSecondPerson(anotherPerson, currentPerson))
                .orElse(dialogsRepository.findDialogByFirstPersonAndSecondPerson(currentPerson, anotherPerson)
                        .orElse(createNewDialog(currentPerson, anotherPerson)));
        dialogsRepository.save(dialog);
        return new CommonRs<>(new ComplexRs(dialogsRepository.countAllByFirstPersonOrSecondPerson(currentPerson, currentPerson)));
    }

    private Dialog createNewDialog(Person currentPerson, Person anotherPerson) {
        return new Dialog(currentPerson, anotherPerson, ZonedDateTime.now());
    }

    public CommonRs<List<DialogRs>> getAllDialogs() {
        List<DialogRs> dialogRsList = createDialogRsList(currentUserExtractor.getPerson());
        dialogRsList = blockDialogs(dialogRsList);
        return new CommonRs<>(dialogRsList, (long) dialogRsList.size());
    }

    public CommonRs<List<MessageRs>> getMessages(Long dialogId) {
        List<MessageRs> messagesRs = new ArrayList<>();
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesRs.add(dialogMapper.toMessageRs(m, dialogMapService.getRecipientForLastMessage(m)));
                });
        return new CommonRs<>(messagesRs
                .stream()
                .sorted(Comparator.comparing(MessageRs::getTime))
                .collect(Collectors.toList()));
    }

    private List<DialogRs> createDialogRsList(Person person) {
        List<DialogRs> dialogRsList = new ArrayList<>();
        for (Dialog d : dialogsRepository.findAllByFirstPersonOrSecondPerson(person, person)) {
            dialogRsList.add(dialogMapper.toDialogRs(createLastMessageRs(d), d));
        }
        return dialogRsList;
    }

    private MessageRs createLastMessageRs(Dialog dialog) {
        try {
            Message message = getLastMessage(dialog.getId());
            return dialogMapper.toMessageRs(message, dialogMapService.getRecipientForLastMessage(message));
        } catch (Exception e) {
            Person currentPerson = currentUserExtractor.getPerson();
            Person anotherPerson = dialogMapService.getRecipientFromDialog(currentPerson.getId(), dialog.getId());
            return MessageRs.builder()
                    .authorId(currentPerson.getId())
                    .recipientId(anotherPerson.getId())
                    .recipient(personMapper.toPersonResponse(anotherPerson))
                    .build();
        }
    }

    private Message getLastMessage(Long dialogId) {
        return messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .stream()
                .max(Comparator.comparing(Message::getTime)).orElseThrow();
    }

    private List<DialogRs> blockDialogs(List<DialogRs> dialogs) {
        Person me = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        List<Friendship> friendships =
                friendshipsRepository.findFriendshipsByFriendshipStatusAndSrcPersonIdOrDstPersonId(FriendshipStatusTypes.BLOCKED, me.getId(), me.getId());
        Set<Long> srcDstPersonsIds =  getSrcDstPersonsIds(friendships, me);
        return dialogs
                .stream()
                .filter(dialog -> !srcDstPersonsIds.contains(dialog.getAuthorId()) && !srcDstPersonsIds.contains(dialog.getRecipientId()))
                .collect(Collectors.toList());
    }

    private Set<Long> getSrcDstPersonsIds(List<Friendship> friendships, Person me) {
        Set<Long> ids = new HashSet<>();
        friendships.forEach(friendship -> {
            if (friendship.getDstPerson().getId().equals(me.getId())) {
                ids.add(friendship.getSrcPerson().getId());
            } else {
                ids.add(friendship.getDstPerson().getId());
            }
        });
        return ids;
    }
}
