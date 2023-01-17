package soialNetworkApp.model.entities.interfaces;

import org.mapstruct.Named;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.NotificationTypes;

public interface Notificationed {

    @Named("getNotificationType")
    NotificationTypes getNotificationType();

    Person getAuthor();

    @Named("getNotificationedId")
    Long getId();
}
