package gusil.mybox.service;

import gusil.mybox.domain.User;
import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.dto.response.ReadUserResponse;
import gusil.mybox.exception.UserNotFoundException;
import gusil.mybox.mapper.UserMapper;
import gusil.mybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<CreateUserResponse> createUser(CreateUserRequest request) {
        User newUser = mapper.mapToUser(request);
        Mono<User> savedUser = repository.save(newUser);
        return savedUser.map(mapper::mapToCreateUserResponse);
    }

    @Override
    public Mono<ReadUserResponse> readUser(String userId) {
        return Mono
                .just(userId)
                .flatMap(repository::findById)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)))
                .map(mapper::mapToReadUserResponse);
    }

    @Override
    public Mono<User> addUserCurrentUsage(String userId, Long usage) {
        return Mono
                .just(userId)
                .flatMap(repository::findById)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)))
                .map(user -> {
                    user.setCurrentUsage(user.getCurrentUsage() + usage);
                    return user;
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Boolean> userExists(String userId) {
        return Mono
                .just(userId)
                .flatMap(repository::existsById);
    }
}
