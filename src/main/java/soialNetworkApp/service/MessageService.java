package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.util.CurrentUser;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessagesRepository messagesRepository;
    private final CurrentUser currentUser;

    @Named("getUnreadMessagesCountForDialog")
    public Long getUnreadMessagesCountForDialog (Dialog dialog) {
        return messagesRepository.findAllByRecipientAndIsDeletedFalse(currentUser.getPerson()).stream()
                .filter(m -> m.getDialog().getId().equals(dialog.getId()) &&
                        m.getReadStatus().equals(ReadStatusTypes.SENT))
                .count();
    }
}
