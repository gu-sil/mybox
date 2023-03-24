package gusil.mybox.service;

import gusil.mybox.domain.Directory;
import gusil.mybox.domain.User;
import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import gusil.mybox.exception.*;
import gusil.mybox.mapper.DirectoryMapper;
import gusil.mybox.repository.DirectoryRepository;
import gusil.mybox.repository.FileRepository;
import gusil.mybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryServiceImpl implements DirectoryService {
    private final DirectoryRepository repository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
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
                // Check directory name is duplicated
                .flatMap(req ->
                        repository.existsByDirectoryNameAndDirectoryParent(
                                req.getDirectoryName(),
                                req.getDirectoryParent()
                        )
                )
                .map(isDuplicated -> {
                    if (isDuplicated) {
                        throw new NameDuplicatedException(request.getDirectoryName());
                    } else {
                        return request;
                    }
                })
                .map(mapper::mapToDirectory)
                .flatMap(repository::save)
                .map(mapper::mapToCreateDirectoryResponse);
    }

    @Override
    public Mono<ReadDirectoryItemListResponse> readDirectoryItemList(String directoryId, String userId, String userName) {
        return Mono
                .just(userId)
                // Check user has userId
                .flatMap(userRepository::findById)
                .map(user -> {
                    if (user.getUsername().equals(userName)) {
                        return directoryId;
                    } else {
                        throw new UnauthorizedUserException(userId);
                    }
                })
                // Check directory exists
                .flatMap(repository::existsById)
                .map(directoryExists -> {
                    if (directoryExists) {
                        return directoryId;
                    } else {
                        throw new DirectoryNotFoundException(directoryId);
                    }
                })
                // Check user has authority
                .flatMap(repository::findById)
                .map(directory -> {
                    if (directory.getDirectoryOwner().equals(userId)) {
                        return new ReadDirectoryItemListResponse();
                    } else {
                        throw new UnauthorizedUserException(userId);
                    }
                })
                .flatMap(response -> repository.findAllByDirectoryParent(directoryId).collectList()
                        .map(directoryList -> {
                            directoryList.forEach(directory -> response.getItems().add(mapper.mapToReadDirectoryItemListResponseItem(directory)));
                            return response;
                        }))
                .flatMap(response -> fileRepository.findAllByFileParent(directoryId).collectList()
                        .map(fileList -> {
                            fileList.forEach(file -> response.getItems().add(mapper.mapToReadDirectoryItemListResponseItem(file)));
                            return response;
                        }));
    }

    @Override
    public Mono<ReadDirectoryItemListResponse> readRootDirectoryItemList(String directoryId, String userId, String userName) {
        return Mono
                .just(userId)
                // Check user has userId
                .flatMap(userRepository::findById)
                .map(user -> {
                    if (user.getUsername().equals(userName)) {
                        return new ReadDirectoryItemListResponse();
                    } else {
                        throw new UnauthorizedUserException(userId);
                    }
                })
                .flatMap(response -> repository.findAllByDirectoryParentAndDirectoryOwner(directoryId, userId).collectList()
                        .map(directoryList -> {
                            directoryList.forEach(directory -> response.getItems().add(mapper.mapToReadDirectoryItemListResponseItem(directory)));
                            return response;
                        }))
                .flatMap(response -> fileRepository.findAllByFileParentAndFileOwner(directoryId, userId).collectList()
                        .map(fileList -> {
                            fileList.forEach(file -> response.getItems().add(mapper.mapToReadDirectoryItemListResponseItem(file)));
                            return response;
                        }));
    }

    @Override
    public Mono<Void> deleteDirectory(String directoryId) {
        return Mono
                .just(directoryId)
                .flatMap(repository::existsById)
                .map(directoryExists -> {
                    if (directoryExists) {
                        return directoryId;
                    } else {
                        throw new DirectoryNotFoundException(directoryId);
                    }
                })
                .flatMap(fileRepository::existsByFileParent)
                .map(fileExist -> {
                    if (fileExist) {
                        throw new DirectoryHasChildException(directoryId);
                    } else {
                        return directoryId;
                    }
                })
                .flatMap(repository::existsByDirectoryParent)
                .map(directoryExists -> {
                    if (directoryExists) {
                        throw new DirectoryHasChildException(directoryId);
                    } else {
                        return directoryId;
                    }
                })
                .flatMap(repository::deleteById)
                .then()
                .log();
    }

    @Override
    public Mono<Boolean> directoryExists(String directoryId) {
        return repository.existsById(directoryId);
    }

    @Override
    public Mono<Boolean> userOwnsDirectory(String username, String directoryId) {
        return Mono
                .zip(userRepository.findByUserName(username), repository.findById(directoryId))
                .map(userAndDirectory -> {
                    User user = userAndDirectory.getT1();
                    Directory directory = userAndDirectory.getT2();

                    return directory.getDirectoryOwner().equals(user.getUserId());
                });
    }
}
