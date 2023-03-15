package gusil.mybox.service;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import gusil.mybox.mapper.FileMapper;
import gusil.mybox.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
                .flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename())).thenReturn(fp))
                .flatMap(fp -> createFileEntity(request))
                .flatMap(response -> userService
                        .addUserCurrentUsage(response.getFileOwnerId(), response.getFileSize())
                        .thenReturn(response));
    }

    private Mono<UploadFileResponse> createFileEntity(UploadFileRequest request) {
        return Mono.just(request)
                .map(mapper::mapToFile)
                .flatMap(repository::save)
                .map(mapper::mapToUploadFileResponse);
    }
}
