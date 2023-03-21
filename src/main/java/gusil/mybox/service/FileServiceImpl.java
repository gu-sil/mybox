package gusil.mybox.service;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import gusil.mybox.exception.FileNotFoundException;
import gusil.mybox.exception.NameDuplicatedException;
import gusil.mybox.mapper.FileMapper;
import gusil.mybox.repository.DirectoryRepository;
import gusil.mybox.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileMapper mapper;
    private final FileRepository repository;
    private final DirectoryRepository directoryRepository;
    private final UserService userService;
    private final Path basePath = Paths.get("../mybox-files/");

    @Override
    public Mono<UploadFileResponse> uploadFile(UploadFileRequest request) {
        return Mono
                .zip(createFileEntity(request), request.getFilePart())
                .flatMap(fileAndFilePart -> userService
                        .addUserCurrentUsage(
                                fileAndFilePart.getT1().getFileOwnerId(),
                                fileAndFilePart.getT1().getFileSize())
                        .thenReturn(fileAndFilePart))
                .flatMap(fileAndFilePart ->
                        fileAndFilePart
                                .getT2()
                                .transferTo(basePath.resolve(fileAndFilePart.getT1().getFileId()))
                                .thenReturn(fileAndFilePart.getT1()))
                .publishOn(Schedulers.boundedElastic())
                .doOnError(ex -> userService
                        .addUserCurrentUsage(request.getUserId(), request.getFileSize() * -1)
                        .block());
    }

    @Override
    public Mono<InputStream> downloadFile(String directoryId, String fileId) {
        return Mono
                .just(fileId)
                .flatMap(id -> repository.existsByFileIdAndFileParent(fileId, directoryId))
                .map(fileExists -> {
                    if (fileExists) {
                        return fileId;
                    }
                    else {
                        throw new FileNotFoundException(fileId);
                    }
                })
                .flatMap(repository::findById)
                .publishOn(Schedulers.boundedElastic())
                .map(file -> {
                    try {
                        return Files.newInputStream(basePath.resolve(file.getFileName()));
                    } catch (IOException e) {
                        throw new RuntimeException(fileId);
                    }
                });
    }

    @Override
    public Mono<Void> deleteFile(String fileId) {
        return Mono
                .just(fileId)
                .flatMap(repository::existsById)
                .map(fileExists -> {
                    if (fileExists) {
                        return fileId;
                    }
                    else {
                        throw new FileNotFoundException(fileId);
                    }
                })
                .flatMap(repository::deleteById);
    }

    private Mono<UploadFileResponse> createFileEntity(UploadFileRequest request) {
        return Mono.just(request)
                .flatMap(req -> repository.existsByFileNameAndFileParent(req.getFileName(), req.getFileParent()))
                .map(fileExists -> {
                    if (fileExists) {
                        throw new NameDuplicatedException(request.getFileName());
                    }
                    else {
                        return request;
                    }
                })
                .flatMap(req -> directoryRepository.existsByDirectoryNameAndDirectoryParent(req.getFileName(), req.getFileParent()))
                .map(fileExists -> {
                    if (fileExists) {
                        throw new NameDuplicatedException(request.getFileName());
                    }
                    else {
                        return request;
                    }
                })
                .map(mapper::mapToFile)
                .flatMap(repository::save)
                .map(mapper::mapToUploadFileResponse);
    }
}
