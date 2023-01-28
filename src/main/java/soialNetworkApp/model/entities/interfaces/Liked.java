package soialNetworkApp.model.entities.interfaces;

import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.LikeTypes;

public interface Liked {

    Long getId();

    Person getAuthor();

    LikeTypes getType();

    String getParentName();
}
