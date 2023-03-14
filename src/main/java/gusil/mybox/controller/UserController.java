package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.dto.response.ReadUserResponse;
import reactor.core.publisher.Mono;

public interface UserController {
    Mono<CreateUserResponse> createUser(CreateUserRequest request);
    Mono<ReadUserResponse> readUser(String userId);
}
