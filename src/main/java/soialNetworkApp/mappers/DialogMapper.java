package soialNetworkApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.api.response.DialogRs;
import soialNetworkApp.api.response.MessageRs;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.DialogMapService;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", uses = {DialogMapService.class}, imports = {ZonedDateTime.class, ReadStatusTypes.class})
public interface DialogMapper {

    @Mapping(target = "id", source = "dialog.id")
    @Mapping(target = "lastMessage", source = "messageRs")
    @Mapping(target = "unreadCount", source = "dialog", qualifiedByName = "getUnreadMessagesCountForDialog")
    DialogRs toDialogRs(MessageRs messageRs, Dialog dialog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dialog", source = "dialog")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "recipient", source = "messageWsRq", qualifiedByName = "getRecipientFromDialog")
    @Mapping(target = "isDeleted", expression = "java(false)")
    Message toMessageFromWs(MessageWsRq messageWsRq, Dialog dialog, Person author);

    default ZonedDateTime getTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    default ReadStatusTypes getReadStatus(String readStatus) {
        return ReadStatusTypes.valueOf(readStatus);
    }

}
