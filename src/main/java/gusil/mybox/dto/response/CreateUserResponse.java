package gusil.mybox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserResponse {
    private String userId;
    private String userName;
    private String userPassword;
    private Long maxUsage;
    private Long currentUsage;
}
