package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.dto.response.ReadUserResponse;
import gusil.mybox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Override
    @GetMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<ReadUserResponse> readUser(@PathVariable String userId) {
        return userService.readUser(userId);
    }
}
