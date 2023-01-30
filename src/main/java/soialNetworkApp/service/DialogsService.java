package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.request.DialogUserShortListDto;
import soialNetworkApp.api.request.MessageRq;
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

    public CommonRs<ComplexRs> deleteDialog(Long dialogId) {
        return null;
    }

    public CommonRs<ComplexRs> deleteMessage(Long dialogId, Long messageId) {
        Message message = messagesRepository.findById(messageId).orElseThrow();
        if (!isMyMessage(message)) {
            return new CommonRs<>(new ComplexRs("Сообщение не может быть удалено!"));
        }
        Dialog dialog = dialogsRepository.findById(dialogId).orElseThrow();
        if (messageId.equals(dialog.getLastMessage().getId())) {
            dialog.setLastMessage(null);
            dialogsRepository.save(dialog);
        }
        message.setIsDeleted(true);
        messagesRepository.save(message);
        if (dialog.getLastMessage() == null) {
            dialog.setLastMessage(getLastMessage(dialogId));
            dialogsRepository.save(dialog);
        }
        return new CommonRs<>(new ComplexRs("Сообщение удалено"));
    }

    public CommonRs<MessageRs> editMessage(Long messageId, MessageRq messageRq) {
        Message message = messagesRepository.findById(messageId).orElseThrow();
        message.setMessageText(messageRq.getMessageText());
        messagesRepository.save(message);
        return new CommonRs<>(dialogMapper.toMessageRs(message, currentUserExtractor.getPerson()));
    }

    private boolean isMyMessage(Message message) {
        return message.getAuthor().equals(currentUserExtractor.getPerson());
    }

    public CommonRs<ComplexRs> getUnreadMessages() {
        return new CommonRs<>(new ComplexRs((long) messagesRepository
                .findAllByRecipientAndReadStatusAndIsDeletedFalse(currentUserExtractor.getPerson(), ReadStatusTypes.SENT)
                .size()));
    }

    public CommonRs<ComplexRs> setReadMessages(Long dialogId) {
        final Long[] readCount = {0L};
        messagesRepository.findAllByDialogIdAndRecipientAndReadStatusAndIsDeletedFalse(dialogId, currentUserExtractor.getPerson(), ReadStatusTypes.SENT)
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesRepository.save(m);
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
                .forEach(m -> messagesRs.add(dialogMapper.toMessageRs(m, dialogMapService.getRecipientForLastMessage(m))));
        return new CommonRs<>(messagesRs.stream()
                .sorted(Comparator.comparing(MessageRs::getTime))
                .collect(Collectors.toList()), (long) messagesRs.size());
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
            Message message = dialog.getLastMessage();
            return dialogMapper.toMessageRs(message, dialogMapService.getRecipientForLastMessage(message));
        } catch (Exception e) {
            Person currentPerson = currentUserExtractor.getPerson();
            Person anotherPerson = getRecipientFromDialog(currentPerson.getId(), dialog.getId());
            return MessageRs.builder()
                    .authorId(currentPerson.getId())
                    .recipientId(anotherPerson.getId())
                    .recipient(personMapper.toPersonResponse(anotherPerson))
                    .build();
        }
    }

        public Person getRecipientFromDialog(Long authorId, Long dialogId) {
        Dialog dialog = dialogsRepository.findById(dialogId).orElseThrow();
        return !authorId.equals(dialog.getFirstPerson().getId()) ?
                dialog.getFirstPerson() :
                dialog.getSecondPerson();
    }

    private Message getLastMessage(Long dialogId) {
        return messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .stream()
                .max(Comparator.comparing(Message::getTime)).orElse(null);
    }

    private List<DialogRs> blockDialogs(List<DialogRs> dialogs) {
        Person me = currentUserExtractor.getPerson();
        List<Friendship> friendships =
                friendshipsRepository.findFriendshipsByFriendshipStatusAndSrcPersonIdOrDstPersonId(FriendshipStatusTypes.BLOCKED, me.getId(), me.getId());
        Set<Long> srcDstPersonsIds = getSrcDstPersonsIds(friendships, me);
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
