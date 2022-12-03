package main.model.entities.interfaces;

import main.model.entities.Person;
import main.model.enums.LikeTypes;

public interface Liked {

    Long getId();

    Person getAuthor();

    LikeTypes getType();
}
