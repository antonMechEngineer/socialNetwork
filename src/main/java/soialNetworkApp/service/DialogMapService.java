package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.service.util.CurrentUserExtractor;

@Service
@RequiredArgsConstructor
public class DialogMapService {

    private final MessagesRepository messagesRepository;
    private final CurrentUserExtractor currentUserExtractor;
    private final DialogsRepository dialogsRepository;

    @Named("getUnreadMessagesCountForDialog")
    public Long getUnreadMessagesCountForDialog (Dialog dialog) {
        return messagesRepository.findAllByRecipientAndIsDeletedFalse(currentUserExtractor.getPerson()).stream()
                .filter(m -> m.getDialog().getId().equals(dialog.getId()) &&
                        m.getReadStatus().equals(ReadStatusTypes.SENT))
                .count();
    }

    @Named("getRecipientFromDialog")
    public Person getRecipientFromDialog(MessageWs messageWsRq) {
        return getRecipientFromDialog(messageWsRq.getAuthorId(), messageWsRq.getDialogId());
    }

    @Named("getRecipientIdFromDialog")
    public Long getRecipientIdFromDialog(MessageWs messageWsRq) {
        return getRecipientFromDialog(messageWsRq.getAuthorId(), messageWsRq.getDialogId()).getId();
    }

    public Person getRecipientFromDialog(Long authorId, Long dialogId) {
        Dialog dialog = dialogsRepository.findById(dialogId).orElseThrow();
        return !authorId.equals(dialog.getFirstPerson().getId()) ?
                dialog.getFirstPerson() :
                dialog.getSecondPerson();
    }

    @Named("isAuthor")
    public Boolean isAuthor(Message message) {
        return message.getAuthor().getId().equals(currentUserExtractor.getPerson().getId());
    }

    public Person getRecipientForLastMessage(Message message) {
        return isAuthor(message) ? message.getRecipient() : message.getAuthor();
    }
    @Named("getRecipientIdForLastMessage")
    public Long getRecipientIdForLastMessage(Message message) {
        return getRecipientForLastMessage(message).getId();
    }

}
