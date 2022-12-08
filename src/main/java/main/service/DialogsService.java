package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.api.request.DialogUserShortListDto;
import main.api.request.MessageRq;
import main.api.request.MessageWsRq;
import main.api.response.*;
import main.mappers.PersonMapper;
import main.model.entities.Dialog;
import main.model.entities.Message;
import main.model.entities.Person;
import main.model.enums.ReadStatusTypes;
import main.repository.DialogsRepository;
import main.repository.MessagesRepository;
import main.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogsService {

    private final DialogsRepository dialogsRepository;
    private final MessagesRepository messagesRepository;

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;


    public CommonResponse<ComplexRs> getUnreadMessage() {
        return CommonResponse.<ComplexRs>builder()
                .data(ComplexRs.builder()
                        .count(unreadMessageCount(findCurrentUser()))
                        .message("Непрочитанные сообщения").build())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private Integer unreadMessageCount(Person person) {
        return messagesRepository.findAllByRecipientAndAndIsDeletedIsFalse(person).size();
    }

    public CommonResponse<ComplexRs> beginDialog(DialogUserShortListDto dialogUserShortListDto) {
        Person dialogPerson = personsRepository.findPersonById(dialogUserShortListDto.getUserIds().get(0)).orElseThrow();
        Dialog dialog = dialogsRepository.findDialogByFirstPerson(dialogPerson)
                .orElse(dialogsRepository.findDialogBySecondPerson(dialogPerson)
                        .orElse(createNewDialog(dialogPerson)));
        dialogsRepository.save(dialog);
        return CommonResponse.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    private Dialog createNewDialog(Person dialogPerson) {
        Dialog dialog = new Dialog();
        dialog.setFirstPerson(findCurrentUser());
        dialog.setSecondPerson(dialogPerson);
        dialog.setLastActiveTime(ZonedDateTime.now());
        return dialog;
    }

    public CommonResponse<List<DialogRs>> getAllDialogs() {
        List<DialogRs> dialogRsList = createDialogRsList(findCurrentUser());
        return CommonResponse.<List<DialogRs>>builder()
                .total((long) dialogRsList.size())
                .timestamp(System.currentTimeMillis())
                .data(dialogRsList)
                .build();
    }

    public void getMessageFromWs(MessageWsRq messageWsRq) {
        Message message = Message.builder()
                .dialog(dialogsRepository.findById(messageWsRq.getDialogId()).orElseThrow())
                .author(personsRepository.findById(messageWsRq.getAuthorId()).orElseThrow())
                .messageText(messageWsRq.getMessageText())
                .time(messageWsRq.getTime().toLocalDateTime())
                .readStatus(ReadStatusTypes.valueOf(messageWsRq.getReadStatus()))
                .recipient(getRecipientFromDialog(messageWsRq.getAuthorId(), messageWsRq.getDialogId()))
                .isDeleted(false)
                .build();
        messagesRepository.save(message);
    }

    public CommonResponse<List<MessageRs>> getMessages(Long dialogId) {
        List<MessageRs> messagesRs = new ArrayList<>();
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .forEach(m -> messagesRs.add(createMessageRs(m)));
        return CommonResponse.<List<MessageRs>>builder()
                .timestamp(System.currentTimeMillis())
                .data(messagesRs).build();
    }

    public CommonResponse<MessageRs> getLastMessageRs(Long dialogId, MessageRq messageRq) {
        if (getLastMessage(dialogId).getMessageText().equals(messageRq.getMessageText())) {
            return CommonResponse.<MessageRs>builder()
                    .data(createLastMessageRs(dialogsRepository.findById(dialogId).orElseThrow()))
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
        return null;
    }

    private Person getRecipientFromDialog(Long authorId, Long dialogId) {
        Dialog dialog = dialogsRepository.findById(dialogId).orElseThrow();
        return !authorId.equals(dialog.getFirstPerson().getId()) ?
                dialog.getFirstPerson() :
                dialog.getSecondPerson();
    }

    private Person findCurrentUser() {
        return personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }

    private List<DialogRs> createDialogRsList(Person person) {
        List<DialogRs> dialogRsList = new ArrayList<>();
        List<Dialog> dialogs = getDialogsByPersonId(person.getId());
        for (Dialog d : dialogs) {
            MessageRs messageRs = createLastMessageRs(d);
            DialogRs dialogRs = DialogRs.builder()
                    .id(d.getId())
                    .authorId(messageRs.getAuthorId())
                    .recipientId(messageRs.getRecipientId())
                    .lastMessage(messageRs)
                    .build();
            if (messageRs.getReadStatus() != null) {
                dialogRs.setReadStatus(messageRs.getReadStatus());
            }
            dialogRsList.add(dialogRs);
        }
        return dialogRsList;
    }

    private MessageRs createLastMessageRs(Dialog dialog) {
        try {
            return createMessageRs(getLastMessage(dialog.getId()));
        } catch (Exception e) {
            return MessageRs.builder()
                    .authorId(dialog.getFirstPerson().getId())
                    .recipientId(dialog.getSecondPerson().getId())
                    .recipient(personMapper.toPersonResponse(dialog.getSecondPerson()))
                    .build();
        }
    }

    private Message getLastMessage(Long dialogId) {
        return messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .stream()
                .max(Comparator.comparing(Message::getTime)).orElseThrow();
    }

    private MessageRs createMessageRs(Message message) {
        return MessageRs.builder()
                .id(message.getId())
                .time(message.getTime())
                .isSentByMe(isAuthor(findCurrentUser(), message))
                .authorId(message.getAuthor().getId())
                .recipientId(message.getRecipient().getId())
                .messageText(message.getMessageText())
                .readStatus(message.getReadStatus().name())
                .recipient(personMapper.toPersonResponse(message.getRecipient()))
                .build();
    }

    private List<Dialog> getDialogsByPersonId(Long personId) {
        return dialogsRepository.findAll().stream()
                .filter(d -> d.getFirstPerson().getId().equals(personId) || d.getSecondPerson().getId().equals(personId))
                .collect(Collectors.toList());
    }

    private Boolean isAuthor(Person person, Message message) {
        return message.getAuthor().getId().equals(person.getId());
    }


}
