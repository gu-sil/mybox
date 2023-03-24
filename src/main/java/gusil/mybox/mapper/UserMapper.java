package gusil.mybox.mapper;

import gusil.mybox.domain.User;
import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.dto.response.ReadUserResponse;
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
                .userName(user.getUsername())
                .rootDirectory(user.getRootDirectory())
                .maxUsage(user.getMaxUsage())
                .currentUsage(user.getCurrentUsage())
                .build();
    }

    public ReadUserResponse mapToReadUserResponse(User user) {
        return ReadUserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .rootDirectory(user.getRootDirectory())
                .maxUsage(user.getMaxUsage())
                .currentUsage(user.getCurrentUsage())
                .build();
    }
}
