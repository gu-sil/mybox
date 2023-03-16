package gusil.mybox.service;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.exception.DirectoryNotFoundException;
import gusil.mybox.exception.UserNotFoundException;
import gusil.mybox.mapper.DirectoryMapper;
import gusil.mybox.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {
    private final DirectoryRepository repository;
    private final UserService userService;
    private final DirectoryMapper mapper;

    @Override
    public Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request) {
        return Mono
                .just(request)
                .flatMap(req -> userService.userExists(req.getDirectoryOwner()))
                // Check directory owner id exists
                .map(userExists -> {
                    if (!userExists) {
                        throw new UserNotFoundException(request.getDirectoryOwner());
                    } else {
                        return request;
                    }
                })
                .flatMap(req -> repository.existsById(req.getDirectoryParent()))
                // Check directory parent id exists
                .map(parentExists -> {
                    if (!parentExists && !request.getDirectoryParent().equals("root")) {
                        throw new DirectoryNotFoundException(request.getDirectoryParent());
                    } else {
                        return request;
                    }
                })
                .map(mapper::mapToDirectory)
                .flatMap(repository::save)
                .map(mapper::mapToCreateDirectoryResponse);
    }
}
