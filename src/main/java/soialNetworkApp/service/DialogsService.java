package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import soialNetworkApp.api.request.DialogUserShortListDto;
import soialNetworkApp.api.response.*;
import soialNetworkApp.mappers.DialogMapper;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.PersonsRepository;
import org.springframework.stereotype.Service;
import soialNetworkApp.service.util.CurrentUser;
import soialNetworkApp.service.util.LastOnlineTime;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogsService {

    private final DialogsRepository dialogsRepository;
    private final MessagesRepository messagesRepository;
    private final PersonsRepository personsRepository;
    private final DialogMapper dialogMapper;
    private final DialogMapService dialogMapService;
    private final CurrentUser currentUser;
    private final PersonMapper personMapper;


    public CommonResponse<ComplexRs> getUnreadMessages() {
        return new CommonResponse<>(new ComplexRs(messagesRepository
                .findAllByRecipientAndIsDeletedFalse(currentUser.getPerson())
                .stream()
                .filter(m -> m.getReadStatus().equals(ReadStatusTypes.SENT))
                .count()));
    }

    public CommonResponse<ComplexRs> setReadMessages(Long dialogId) {
        final Long[] readCount = {0L};
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .stream()
                .filter(m -> m.getRecipient().equals(currentUser.getPerson()))
                .filter(m -> m.getReadStatus().equals(ReadStatusTypes.SENT))
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesRepository.save(m);
                    readCount[0]++;
                });
        return new CommonResponse<>(new ComplexRs(readCount[0]));
    }

    public CommonResponse<ComplexRs> beginDialog(DialogUserShortListDto dialogUserShortListDto) {
        Person currentPerson = currentUser.getPerson();
        Person anotherPerson = personsRepository.findPersonById(dialogUserShortListDto.getUserIds().get(0)).orElseThrow();
        Dialog dialog = (dialogsRepository.findDialogByFirstPersonAndSecondPerson(anotherPerson, currentPerson))
                .orElse(dialogsRepository.findDialogByFirstPersonAndSecondPerson(currentPerson, anotherPerson)
                        .orElse(createNewDialog(currentPerson, anotherPerson)));
        dialogsRepository.save(dialog);
        return new CommonResponse<>(new ComplexRs(dialogsRepository.countAllByFirstPersonOrSecondPerson(currentPerson, currentPerson)));
    }

    private Dialog createNewDialog(Person currentPerson, Person anotherPerson) {
        return new Dialog(currentPerson, anotherPerson, ZonedDateTime.now());
    }

    public CommonResponse<List<DialogRs>> getAllDialogs() {
        List<DialogRs> dialogRsList = createDialogRsList(currentUser.getPerson());
        return new CommonResponse<>(dialogRsList, (long) dialogRsList.size());
    }

    public CommonResponse<List<MessageRs>> getMessages(Long dialogId) {
        List<MessageRs> messagesRs = new ArrayList<>();
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesRs.add(dialogMapper.toMessageRs(m, dialogMapService.getRecipientForLastMessage(m)));
                });
        return new CommonResponse<>(messagesRs
                .stream()
                .sorted(Comparator.comparing(MessageRs::getTime))
                .collect(Collectors.toList()));
    }

    private List<DialogRs> createDialogRsList(Person person) {
        LastOnlineTime.saveLastOnlineTime(currentUser.getPerson(), personsRepository);
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
            Person currentPerson = currentUser.getPerson();
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
}
