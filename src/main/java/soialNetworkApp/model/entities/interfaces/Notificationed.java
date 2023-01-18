package soialNetworkApp.model.entities.interfaces;

import org.mapstruct.Named;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.NotificationTypes;

public interface Notificationed {

    NotificationTypes getNotificationType();

    Person getAuthor();

    Long getId();
}
