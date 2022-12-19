package soialNetworkApp.mappers;

import soialNetworkApp.api.response.NotificationResponse;
import soialNetworkApp.model.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface NotificationMapper {

    @Mapping(target = "info", constant = "notificationInfo")
    @Mapping(target = "notificationType", source = "entity.notificationType")
    @Mapping(target = "entityAuthor", source = "entity.author")
    NotificationResponse toNotificationResponse(Notification notification);
}
