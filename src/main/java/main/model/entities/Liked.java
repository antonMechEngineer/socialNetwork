package main.model.entities;

import main.model.enums.LikeTypes;

public interface Liked {

    Long getId();

    Person getAuthor();

    LikeTypes getType();
}
