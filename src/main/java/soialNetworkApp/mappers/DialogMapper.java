package soialNetworkApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.api.response.DialogRs;
import soialNetworkApp.api.response.MessageRs;
import soialNetworkApp.api.response.MessageTypingWsRs;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.service.DialogMapService;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", uses = {DialogMapService.class, PersonMapper.class}, imports = {ZonedDateTime.class, ReadStatusTypes.class})
public interface DialogMapper {

    @Mapping(target = "id", source = "dialog.id")
    @Mapping(target = "lastMessage", source = "messageRs")
    @Mapping(target = "unreadCount", source = "dialog", qualifiedByName = "getUnreadMessagesCountForDialog")
    DialogRs toDialogRs(MessageRs messageRs, Dialog dialog);

    @Mapping(target = "isSentByMe", source="message", qualifiedByName = "isAuthor")
    @Mapping(target = "recipientId", source="message", qualifiedByName = "getRecipientIdForLastMessage")
    @Mapping(target = "recipient", source = "person", qualifiedByName = "toPersonRs")
    @Mapping(target = "authorId", source = "message.author.id")
    @Mapping(target = "readStatus", expression = "java(message.getReadStatus().name())")
    @Mapping(target = "id", source = "message.id")
    MessageRs toMessageRs(Message message, Person person);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recipient", source = "messageWsRq", qualifiedByName = "getRecipientFromDialog")
    @Mapping(target = "isDeleted", expression = "java(false)")
    Message toMessageFromWs(MessageWsRq messageWsRq, Dialog dialog, Person author);

    MessageTypingWsRs toMessageTypingWsRs(Long userId, Long dialogId, Boolean typing);

    default ZonedDateTime getTime(Long time) {
        return new Timestamp(time).toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    default ReadStatusTypes getReadStatus(String readStatus) {
        return ReadStatusTypes.valueOf(readStatus);
    }

}