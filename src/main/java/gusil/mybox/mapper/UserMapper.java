package gusil.mybox.mapper;

import gusil.mybox.domain.User;
import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static final long MAX_USAGE = 53687091200L;

    public User mapToUser(CreateUserRequest request) {
        return User.builder()
                .userName(request.getUserName())
                .userPassword(request.getUserPassword())
                .maxUsage(MAX_USAGE)
                .currentUsage(0L)
                .build();
    }

    public CreateUserResponse mapToCreateUserResponse(User user) {
        return CreateUserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userPassword(user.getUserPassword())
                .maxUsage(user.getMaxUsage())
                .currentUsage(user.getCurrentUsage())
                .build();
    }
}
