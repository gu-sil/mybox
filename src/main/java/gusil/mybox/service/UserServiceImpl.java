package gusil.mybox.service;

import gusil.mybox.domain.User;
import gusil.mybox.dto.request.CreateUserRequest;
import gusil.mybox.dto.response.CreateUserResponse;
import gusil.mybox.mapper.UserMapper;
import gusil.mybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<CreateUserResponse> createUser(CreateUserRequest request) {
        User newUser = mapper.mapToUser(request);
        Mono<User> savedUser = repository.save(newUser);
        return savedUser.map(mapper::mapToCreateUserResponse);
    }
}
