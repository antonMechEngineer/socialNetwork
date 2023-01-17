package soialNetworkApp.mappers;

import soialNetworkApp.api.response.NotificationRs;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface NotificationMapper {

    @Mapping(target = "info", constant = "notificationInfo")
    @Mapping(target = "notificationType", source = "entity.notificationType")
    @Mapping(target = "entityAuthor", source = "entity.author")
    NotificationRs toNotificationResponse(Notification notification);

    @Mapping(target = "notificationType", source = "entity.notificationType")
    @Mapping(target = "notificationedId", source = "entity.id")
    @Mapping(target = "personId", source = "person.id")
    NotificationKafka toNotificationKafka(Notification notification);

    @Mapping(target = "id", expression = "java(0L)")
    @Mapping(target = "notificationType", source = "entity", qualifiedByName = "getNotificationType")
    @Mapping(target = "personId", source = "person", qualifiedByName = "getId")
    @Mapping(target = "isRead", expression = "java(false)")
    MessageKafka toNotificationKafkaFromNotificationed(Notificationed notificationed, Person person);

    default LocalDateTime getSentTime(){
        return LocalDateTime.now();
    }
}
