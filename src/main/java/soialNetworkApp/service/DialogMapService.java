package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.service.util.CurrentUser;

@Service
@RequiredArgsConstructor
public class DialogMapService {

    private final MessagesRepository messagesRepository;
    private final CurrentUser currentUser;
    private final DialogsRepository dialogsRepository;

    @Named("getUnreadMessagesCountForDialog")
    public Long getUnreadMessagesCountForDialog (Dialog dialog) {
        return messagesRepository.findAllByRecipientAndIsDeletedFalse(currentUser.getPerson()).stream()
                .filter(m -> m.getDialog().getId().equals(dialog.getId()) &&
                        m.getReadStatus().equals(ReadStatusTypes.SENT))
                .count();
    }

    @Named("getRecipientFromDialog")
    public Person getRecipientFromDialog(MessageWsRq messageWsRq) {
        return getRecipientFromDialog(messageWsRq.getAuthorId(), messageWsRq.getDialogId());
    }

    public Person getRecipientFromDialog(Long authorId, Long dialogId) {
        Dialog dialog = dialogsRepository.findById(dialogId).orElseThrow();
        return !authorId.equals(dialog.getFirstPerson().getId()) ?
                dialog.getFirstPerson() :
                dialog.getSecondPerson();
    }

}
