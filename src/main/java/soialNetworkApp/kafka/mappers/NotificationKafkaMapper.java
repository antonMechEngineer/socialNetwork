package soialNetworkApp.kafka.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.service.DialogMapService;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import soialNetworkApp.model.entities.Message;

@Mapper(componentModel = "spring", uses = {DialogMapService.class})
public interface NotificationKafkaMapper {
    @Mapping(target = "id", expression = "java(0L)")
    @Mapping(target = "recipientId", source = "messageWsRq", qualifiedByName = "getRecipientIdFromDialog")
    @Mapping(target = "isDeleted", expression = "java(false)")
    MessageKafka toNotificationKafkaFromNotificationed(Notificationed entity, Dialog dialog);


    default ZonedDateTime getTime(Long time) {
        return new Timestamp(time).toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    @Named("getRecipientId")
    default Long getRecipientId(Message message) {
        return message.getRecipient().getId();
    }

    @Named("getAuthorId")
    default Long getAuthorId(Message message) {
        return message.getAuthor().getId();
    }

    @Named("getDialogId")
    default Long getDialogId(Message message) {
        return message.getDialog().getId();
    }

}