package soialNetworkApp.model.entities.interfaces;

import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.NotificationTypes;

public interface Notificationed {

    NotificationTypes getNotificationType();

    Person getAuthor();
}
