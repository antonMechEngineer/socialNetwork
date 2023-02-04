package socialnet.model.entities.interfaces;

import socialnet.model.entities.Person;
import socialnet.model.enums.NotificationTypes;

public interface Notificationed {

    NotificationTypes getNotificationType();

    Person getAuthor();

    Long getId();

    String getSimpleInfo();
}
