package gusil.mybox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDirectoryResponse {
    private String directoryId;
    private String directoryName;
    private String directoryOwner;
    private String directoryParent;
    private Long size;
}
