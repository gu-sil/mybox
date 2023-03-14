package gusil.mybox.service;

import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.dto.response.ReadUserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<CreateUserResponse> createUser(CreateUserRequest request);

    Mono<ReadUserResponse> readUser(String userId);
}
