package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.api.request.DialogUserShortListDto;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.DialogRs;
import main.api.response.MessageRs;
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

import java.util.ArrayList;
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

    private Person findCurrentUser() {
        return personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }


    public CommonResponse<List<DialogRs>> getAllDialogs() {
        List<DialogRs> dialogRsList = createListOfDialogRs(findCurrentUser());
        return CommonResponse.<List<DialogRs>>builder()
                .error("success")
                .offset(0)
                .perPage(10)
                .total((long) dialogRsList.size())
                .timestamp(System.currentTimeMillis())
                .data(dialogRsList)
                .build();
    }

    public CommonResponse<ComplexRs> getMessage() {
        ComplexRs complexRs = new ComplexRs();
        return CommonResponse.<ComplexRs>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(complexRs)
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
        getDialogsByUserId(person.getId()).forEach(d -> {
            DialogRs dialogRs = DialogRs.builder()
                    .id(d.getId())
                    .lastMessage(createMessageRs(person, d))
                    .authorId(getPersonIdFromDialog(person, d))
                    .recipientId(getPersonIdFromDialog(person, d))
                    .readStatus(d.getLastMessage().getReadStatus().name())
                    .build();
            if (dialogRs.getUnreadCount() == null) {
                dialogRs.setUnreadCount(0);
            }
            if (dialogRs.getReadStatus().equals(ReadStatusTypes.SENT)) {
                dialogRs.setUnreadCount(dialogRs.getUnreadCount() + 1);
            }
            dialogRsList.add(dialogRs);
        });
        return dialogRsList;
    }

    private List<Dialog> getDialogsByUserId(Long userId) {
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
