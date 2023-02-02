package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.MessagesRepository;

@Service
@RequiredArgsConstructor
public class DialogMapService {

    private final MessagesRepository messagesRepository;
    private final PersonCacheService personCacheService;

    @Named("getUnreadMessagesCountForDialog")
    public Long getUnreadMessagesCountForDialog (Dialog dialog) {
        return messagesRepository.findAllByRecipientAndIsDeletedFalse(personCacheService.getPersonByContext()).stream()
                .filter(m -> m.getDialog().getId().equals(dialog.getId()) &&
                        m.getReadStatus().equals(ReadStatusTypes.SENT))
                .count();
    }

    @Named("isAuthor")
    public Boolean isAuthor(Message message) {
        return message.getAuthor().getId().equals(personCacheService.getPersonByContext().getId());
    }

    public Person getRecipientForLastMessage(Message message) {
        return isAuthor(message) ? message.getRecipient() : message.getAuthor();
    }
}
