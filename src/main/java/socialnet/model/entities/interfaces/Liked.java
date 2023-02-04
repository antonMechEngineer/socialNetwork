package socialnet.model.entities.interfaces;

import socialnet.model.entities.Person;
import socialnet.model.enums.LikeTypes;

public interface Liked {

    Long getId();

    Person getAuthor();

    LikeTypes getType();

    String getParentName();
}
