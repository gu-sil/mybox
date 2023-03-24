package gusil.mybox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadUserResponse {
    private String userId;
    private String userName;
    private String rootDirectory;
    private Long maxUsage;
    private Long currentUsage;
}
