package gusil.mybox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFileResponse {
    private String fileId;
    private String fileName;
    private String fileOwnerId;
    private String fileParent;
    private Long fileSize;
}
