package soialNetworkApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.api.response.DialogRs;
import soialNetworkApp.api.response.MessageRs;
import soialNetworkApp.kafka.dto.MessageKafka;
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

@Mapper(componentModel = "spring", uses = {DialogMapService.class, PersonMapper.class, PersonsRepository.class, DialogsRepository.class})
public interface DialogMapper {

    @Mapping(target = "id", source = "dialog.id")
    @Mapping(target = "lastMessage", source = "messageRs")
    @Mapping(target = "unreadCount", source = "dialog", qualifiedByName = "getUnreadMessagesCountForDialog")
    DialogRs toDialogRs(MessageRs messageRs, Dialog dialog);

    @Mapping(target = "isSentByMe", source="message", qualifiedByName = "isAuthor")
    @Mapping(target = "recipient", source = "person")
    @Mapping(target = "recipientId", source="message.recipient.id")
    @Mapping(target = "authorId", source = "message.author.id")
    @Mapping(target = "readStatus", expression = "java(message.getReadStatus().name())")
    @Mapping(target = "id", source = "message.id")
    MessageRs toMessageRs(Message message, Person person);

    @Mapping(target = "isDeleted", expression = "java(false)")
    MessageKafka toMessageKafkaFromMessageWs(MessageWs messageWs);

    @Mapping(source = "authorId", target = "author")
    @Mapping(source = "recipientId", target = "recipient")
    @Mapping(source = "dialogId", target = "dialog")
    Message toMessageFromKafka(MessageKafka kafka);

    default ZonedDateTime getTime(Long time) {
        return new Timestamp(time).toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    default ReadStatusTypes getReadStatus(String readStatus) {
        return ReadStatusTypes.valueOf(readStatus);
    }
}
