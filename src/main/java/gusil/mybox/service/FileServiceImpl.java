package gusil.mybox.service;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import gusil.mybox.exception.FileNotFoundException;
import gusil.mybox.mapper.FileMapper;
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
    private final UserService userService;
    private final Path basePath = Paths.get("../mybox-files/");

    @Override
    public Mono<UploadFileResponse> uploadFile(UploadFileRequest request) {
        return Mono.just(request)
                .flatMap(UploadFileRequest::getFilePart)
                .flatMap(fp -> fp.transferTo(basePath.resolve(request.getFileName())).thenReturn(fp))
                .flatMap(fp -> createFileEntity(request))
                .flatMap(response -> userService
                        .addUserCurrentUsage(response.getFileOwnerId(), response.getFileSize())
                        .thenReturn(response));
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
                .map(mapper::mapToFile)
                .flatMap(repository::save)
                .map(mapper::mapToUploadFileResponse);
    }
}
