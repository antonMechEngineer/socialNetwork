package main.model.entities.interfaces;

import main.model.entities.Person;
import main.model.enums.NotificationTypes;

public interface Notificationed {

    NotificationTypes getNotificationType();

    Person getAuthor();
}
