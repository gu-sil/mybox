package gusil.mybox.controller;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import gusil.mybox.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
@Slf4j
public class FileControllerImpl implements FileController {
    private final FileService fileService;

    @Override
    @PostMapping("directories/{directoriesId}/files")
    public Mono<UploadFileResponse> uploadFile(
            @RequestPart String userId,
            @RequestPart String fileName,
            @PathVariable("directoriesId") String fileParent,
            @RequestPart Mono<FilePart> filePart,
            @RequestHeader("Content-Length") Long fileSize) {

        return filePart
//                .flatMap(assertUserExists)
//                .flatMap(assertDirectoryExists)
//                .flatMap(assertDuplicatedFileName)
//                .flatMap(assertUserUsageNotExceeds)
                .map(fp -> UploadFileRequest.builder()
                        .userId(userId)
                        .fileName(fileName)
                        .fileParent(fileParent)
                        .filePart(filePart)
                        .fileSize(fileSize)
                        .build())
                .flatMap(fileService::uploadFile);
    }
}
