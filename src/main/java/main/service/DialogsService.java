package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.DialogRs;
import main.api.response.MessageRs;
import main.mappers.PersonMapper;
import main.model.entities.Dialog;
import main.model.entities.Message;
import main.model.entities.Person;
import main.repository.DialogsRepository;
import main.repository.MessagesRepository;
import main.repository.PersonsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogsService {

    private final DialogsRepository dialogsRepository;
    private final MessagesRepository messagesRepository;

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;

    public CommonResponse<List<DialogRs>> getAllDialogs() {
        Person currentUser = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        return CommonResponse.<List<DialogRs>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .total(0L)
                .offset(0)
                .data(createListOfDialogRs(currentUser))
                .itemPerPage(10)
                .errorDescription("")
                .build();
    }

    private MessageRs createMessageRs(Person person, Dialog dialog) {
        Message message = dialog.getLastMessage();
        return MessageRs.builder()
                .id(message.getId())
                .time(message.getTime())
                .isSentByMe(isAuthor(person, message))
                .authorId(message.getAuthor().getId())
                .recipientId(message.getAuthor().getId())
                .messageText(message.getMessageText())
                .readStatus(message.getReadStatus().name())
                .recipient(personMapper.toPersonResponse(message.getRecipient()))
                .build();
    }

    private List<DialogRs> createListOfDialogRs(Person person) {
        List<DialogRs> dialogRsList = new ArrayList<>();
        getDialogsById(person.getId()).forEach(d -> {
            DialogRs dialogRs = DialogRs.builder()
                    .id(d.getId())
                    .unreadCount(0)
                    .lastMessage(createMessageRs(person, d))
                    .authorId(getPersonIdFromDialog(person, d))
                    .recipientId(getPersonIdFromDialog(person, d))
                    .readStatus(d.getLastMessage().getReadStatus().name())
                    .build();
            dialogRsList.add(dialogRs);
        });
        return dialogRsList;
    }

    private List<Dialog> getDialogsById(Long userId) {
        return dialogsRepository.findAll().stream()
                .filter(d -> d.getFirstPerson().getId().equals(userId) || d.getSecondPerson().getId().equals(userId))
                .collect(Collectors.toList());
    }

    private Long getPersonIdFromDialog(Person person, Dialog dialog) {
        return isAuthor(person, dialog.getLastMessage()) ? dialog.getFirstPerson().getId() : dialog.getSecondPerson().getId();
    }

    private Boolean isAuthor(Person person, Message message) {
        return message.getAuthor().getId() == person.getId();
    }
}
