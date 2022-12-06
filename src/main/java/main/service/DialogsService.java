package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.DialogUserShortListDto;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogsService {

    private final DialogsRepository dialogsRepository;
    private final MessagesRepository messagesRepository;

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;


    public CommonResponse<ComplexRs> getMessage() {
        return CommonResponse.<ComplexRs>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().message("test").build())
                .build();
    }
    public CommonResponse<ComplexRs> beginDialog(DialogUserShortListDto dialogUserShortListDto) {
        Person dialogPerson = personsRepository.findPersonById(dialogUserShortListDto.getUserIds().get(0)).orElseThrow();
        Dialog dialog = dialogsRepository.findDialogByFirstPerson(dialogPerson)
                .orElse(dialogsRepository.findDialogBySecondPerson(dialogPerson)
                        .orElse(createNewDialog(dialogPerson)));
        dialogsRepository.save(dialog);
        return CommonResponse.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().message("Написать сообщение").build())
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
//                .total((long) dialogRsList.size())
                .timestamp(System.currentTimeMillis())
                .data(dialogRsList)
                .build();
    }

    private Person findCurrentUser() {
        return personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }

    private List<DialogRs> createDialogRsList(Person person) {
        List<DialogRs> dialogRsList = new ArrayList<>();
        List<Dialog> dialogs = getDialogsByPersonId(person.getId());
        for (Dialog d : dialogs) {
            MessageRs messageRs = createMessageRs(d);
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

    private MessageRs createMessageRs(Dialog dialog) {
        Message message;
        Optional<Message> messageOptional = messagesRepository.findMessageByDialogId(dialog.getId());
        if (messageOptional.isPresent()) {
            message = messageOptional.get();
            return MessageRs.builder()
                    .id(message.getId())
                    .time(message.getTime())
                    .isSentByMe(isAuthor(findCurrentUser(), message))
                    .authorId(message.getAuthor().getId())
                    .recipientId(message.getAuthor().getId())
                    .messageText(message.getMessageText())
                    .readStatus(message.getReadStatus().name())
                    .recipient(personMapper.toPersonResponse(message.getRecipient()))
                    .build();
        }
        return MessageRs.builder()
                .authorId(findCurrentUser().getId())
                .recipientId(dialog.getSecondPerson().getId())
                .messageText("")
                .isSentByMe(true)
                .time(ZonedDateTime.now())
                .readStatus(ReadStatusTypes.SENT.name())
                .recipient(personMapper.toPersonResponse(dialog.getSecondPerson()))
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
