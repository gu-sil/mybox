package gusil.mybox.service;

import gusil.mybox.domain.User;
import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.dto.response.ReadUserResponse;
import gusil.mybox.exception.UserNotFoundException;
import gusil.mybox.mapper.UserMapper;
import gusil.mybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final DirectoryService directoryService;
    private final UserMapper mapper;

    @Override
    @Transactional
    public Mono<CreateUserResponse> createUser(CreateUserRequest request) {
        return Mono
                .just(request)
                .map(mapper::mapToUser)
                .flatMap(user -> {
                    CreateDirectoryRequest createDirectoryRequest =
                            new CreateDirectoryRequest(user.getUserId(), "unassigned", "root");
                    return directoryService.createRootDirectory(createDirectoryRequest);
                })
                .map(rootDirectory -> {
                    User user = mapper.mapToUser(request);
                    user.setRootDirectory(rootDirectory.getDirectoryId());
                    return user;
                })
                .flatMap(repository::save)
                .flatMap(savedUser -> directoryService
                        .updateDirectoryOwner(savedUser.getRootDirectory(), savedUser.getUserId())
                        .thenReturn(savedUser))
                .map(mapper::mapToCreateUserResponse);
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

    @Override
    public Mono<Boolean> currentUsageExceeds(String userId, Long usageToAdd) {
        return Mono
                .just(userId)
                .flatMap(repository::findById)
                .map(user -> user.getCurrentUsage() + usageToAdd > user.getMaxUsage());
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return Mono
                .just(username)
                .flatMap(repository::findByUserName)
                .log();
    }
}
