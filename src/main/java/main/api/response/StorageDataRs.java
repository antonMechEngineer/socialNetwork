package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageDataRs {
    private String id;
    private long ownerId;
    private String fileName;
    private String relativeFilePath;
    private String fileFormat;
    private long bytes;
    private String fileType;
    private long createdAt;
}
