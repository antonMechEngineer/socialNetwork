package main.model.enums;

import java.time.LocalDateTime;

public enum PostTypes {

    POSTED, QUEUED, DELETED;

    public static PostTypes getType(boolean isDeleted, LocalDateTime timeCreation) {
        if (isDeleted) {
            return DELETED;
        }
        return timeCreation.isAfter(LocalDateTime.now()) ? QUEUED : POSTED;
    }
}
