package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import soialNetworkApp.api.request.DialogUserShortListDto;
import soialNetworkApp.api.request.MessageWsRq;
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
import soialNetworkApp.security.jwt.JWTUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.service.util.CurrentUser;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogsService {

    private final DialogsRepository dialogsRepository;
    private final MessagesRepository messagesRepository;

    private final PersonsRepository personsRepository;
    private final PersonMapper personMapper;
    private final DialogMapper dialogMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final JWTUtil jwtUtil;
    private final CurrentUser currentUser;
    private final DialogMapService dialogMapService;


    public CommonResponse<ComplexRs> getUnreadMessages() {
        return CommonResponse.<ComplexRs>builder()
                .data(ComplexRs.builder()
                        .count((int) messagesRepository.findAllByRecipientAndIsDeletedFalse(currentUser.getPerson()).stream()
                                .filter(m -> m.getReadStatus().equals(ReadStatusTypes.SENT))
                                .count())
                        .build())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public CommonResponse<ComplexRs> setReadMessages(Long dialogId) {
        AtomicReference<Integer> readCount = new AtomicReference<>(0);
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId).stream()
                .filter(m -> m.getRecipient().equals(currentUser.getPerson()))
                .filter(m -> m.getReadStatus().equals(ReadStatusTypes.SENT))
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesRepository.save(m);
                    readCount.getAndSet(readCount.get() + 1);
                });
        return CommonResponse.<ComplexRs>builder()
                .data(ComplexRs.builder()
                        .count(readCount.get())
                        .build())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public CommonResponse<ComplexRs> beginDialog(DialogUserShortListDto dialogUserShortListDto) {
        Person dialogPerson = personsRepository.findPersonById(dialogUserShortListDto.getUserIds().get(0)).orElseThrow();
        Dialog dialog = (dialogsRepository.findDialogByFirstPersonAndSecondPerson(dialogPerson, currentUser.getPerson()))
                .orElse(dialogsRepository.findDialogByFirstPersonAndSecondPerson(currentUser.getPerson(), dialogPerson)
                        .orElse(createNewDialog(currentUser.getPerson(), dialogPerson)));
        dialogsRepository.save(dialog);
        // todo какой именно респонс необходимо возвращать? нужно ли в коунт количество диалогов передать? что передать в месадж?
        return CommonResponse.<ComplexRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(ComplexRs.builder().build())
                .build();
    }

    private Dialog createNewDialog(Person currentPerson, Person dialogPerson) {
        return Dialog.builder()
                .firstPerson(currentPerson)
                .secondPerson(dialogPerson)
                .lastActiveTime(ZonedDateTime.now())
                .build();
    }

    public CommonResponse<List<DialogRs>> getAllDialogs() {
        List<DialogRs> dialogRsList = createDialogRsList(currentUser.getPerson());
        return CommonResponse.<List<DialogRs>>builder()
                .total((long) dialogRsList.size())
                .timestamp(System.currentTimeMillis())
                .data(createDialogRsList(currentUser.getPerson()))
                .build();
    }

    public void getMessageFromWs(MessageWsRq messageWsRq) {
        messagingTemplate.convertAndSendToUser(messageWsRq.getDialogId().toString(), "/queue/messages",
                MessageWsRs.builder()
                        .authorId(messageWsRq.getAuthorId())
                        .recipientId(dialogMapService.getRecipientFromDialog(messageWsRq).getId())
                        .isSentByMe(true)
                        .messageText(messageWsRq.getMessageText())
                        .time(System.currentTimeMillis())
                        .build());

        Message message = dialogMapper.toMessageFromWs(messageWsRq,
                dialogsRepository.findById(messageWsRq.getDialogId()).orElseThrow(),
                personsRepository.findPersonById(messageWsRq.getAuthorId()).orElseThrow());
        log.info(message.toString());

        messagesRepository.save(message);
    }

    public void messageTypingFromWs(Long dialogId, Long userId, Boolean typing) {
        Map<String, Object> header = Collections.singletonMap("type", typing ? "start_typing" : "stop_typing");

        messagingTemplate.convertAndSendToUser(dialogId.toString(), "/queue/messages",
                MessageWsRs.builder()
                        .authorId(userId)
                        .recipientId(dialogMapService.getRecipientFromDialog(userId, dialogId).getId())
                        .isSentByMe(true)
                        .build(), header);
    }

    public CommonResponse<List<MessageRs>> getMessages(Long dialogId) {
        List<MessageRs> messagesRs = new ArrayList<>();
        messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .forEach(m -> {
                    m.setReadStatus(ReadStatusTypes.READ);
                    messagesRs.add(createMessageRs(m));
                });
        return CommonResponse.<List<MessageRs>>builder()
                .timestamp(System.currentTimeMillis())
                .data(messagesRs.stream()
                        .sorted(Comparator.comparing(MessageRs::getTime))
                        .collect(Collectors.toList()))
                .build();
    }

    private List<DialogRs> createDialogRsList(Person person) {
        List<DialogRs> dialogRsList = new ArrayList<>();
        List<Dialog> dialogs = getDialogsByPersonId(person.getId());
        for (Dialog d : dialogs) {
            MessageRs messageRs = createLastMessageRs(d);
            dialogRsList.add(dialogMapper.toDialogRs(messageRs, d));
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
                .isSentByMe(isAuthor(currentUser.getPerson(), message))
                .authorId(message.getAuthor().getId())
                .recipientId(getPersonForLastMessage(message).getId())
                .messageText(message.getMessageText())
                .readStatus(message.getReadStatus().name())
                .recipient(personMapper.toPersonResponse(getPersonForLastMessage(message)))
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

    private Person getPersonForLastMessage(Message message) {
        return isAuthor(currentUser.getPerson(), message) ? message.getRecipient() : message.getAuthor();
    }
}
