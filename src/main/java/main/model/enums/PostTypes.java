package main.model.enums;

public enum PostTypes {

    POSTED, QUEUED, DELETED;

    public static PostTypes getType(boolean isDeleted, long timeCreation) {
        if (isDeleted) {
            return DELETED;
        }
        return timeCreation > System.currentTimeMillis() ? QUEUED : POSTED;
    }
}
