package gusil.mybox.service;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import gusil.mybox.exception.DirectoryHasChildException;
import gusil.mybox.exception.DirectoryNotFoundException;
import gusil.mybox.exception.UserNotFoundException;
import gusil.mybox.mapper.DirectoryMapper;
import gusil.mybox.repository.DirectoryRepository;
import gusil.mybox.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {
    private final DirectoryRepository repository;
    private final FileRepository fileRepository;
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

    @Override
    public Mono<ReadDirectoryItemListResponse> readDirectoryItemList(String directoryId) {
        return Mono
                .just(directoryId)
                .flatMap(repository::existsById)
                .map(directoryExists -> {
                    if (directoryExists) {
                        return new ReadDirectoryItemListResponse();
                    } else {
                        throw new DirectoryNotFoundException(directoryId);
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
                        }))
                ;
    }

    @Override
    public Mono<Void> deleteDirectory(String directoryId) {
        return Mono
                .just(directoryId)
                .flatMap(repository::existsById)
                .map(directoryExists -> {
                    if (directoryExists) {
                        return directoryId;
                    }
                    else {
                        throw new DirectoryNotFoundException(directoryId);
                    }
                })
                .flatMap(fileRepository::existsByFileParent)
                .map(fileExist -> {
                    if (fileExist) {
                        throw new DirectoryHasChildException(directoryId);
                    }
                    else {
                        return directoryId;
                    }
                })
                .flatMap(repository::existsByDirectoryParent)
                .map(directoryExists -> {
                    if (directoryExists) {
                        throw new DirectoryHasChildException(directoryId);
                    }
                    else {
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
}
