package socialnet.model.enums;

public enum LikeTypes {

    POST("Post"), COMMENT("Comment");

    private String description;

    LikeTypes(String description) {
        this.description = description;
    }

    public static LikeTypes getType(String description) {
        switch (description) {
            case "Post" : return LikeTypes.POST;
            case "Comment" : return LikeTypes.COMMENT;
            default: return null;
        }
    }
}
