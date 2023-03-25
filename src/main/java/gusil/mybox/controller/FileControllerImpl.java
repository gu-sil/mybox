package gusil.mybox.controller;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import gusil.mybox.exception.DirectoryNotFoundException;
import gusil.mybox.exception.UnauthorizedUserException;
import gusil.mybox.exception.UserUsageExceedsException;
import gusil.mybox.service.DirectoryService;
import gusil.mybox.service.FileService;
import gusil.mybox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
@Slf4j
public class FileControllerImpl implements FileController {
    private final FileService fileService;
    private final DirectoryService directoryService;
    private final UserService userService;

    @Override
    @PostMapping("directories/{directoryId}/files")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<UploadFileResponse> uploadFile(
            @RequestPart String userId,
            @RequestPart String fileName,
            @PathVariable("directoryId") String fileParent,
            @RequestPart Mono<FilePart> filePart,
            @RequestHeader("Content-Length") Long fileSize,
            Authentication auth) {

        return filePart
                .flatMap(fp -> directoryService.userOwnsDirectory(auth.getName(), fileParent))
                .map(userOwnsDirectory -> {
                    if (userOwnsDirectory) {
                        return filePart;
                    } else {
                        throw new UnauthorizedUserException(userId);
                    }
                })
                .flatMap(fp -> directoryService.directoryExists(fileParent))
                .map(directoryExists -> {
                    if (directoryExists) {
                        return filePart;
                    } else {
                        throw new DirectoryNotFoundException(fileParent);
                    }
                })
                .flatMap(fp -> userService.currentUsageExceeds(userId, fileSize))
                .map(usageExceeds -> {
                    if (usageExceeds) {
                        throw new UserUsageExceedsException(userId);
                    } else {
                        return filePart;
                    }
                })
                .map(fp -> UploadFileRequest.builder()
                        .userId(userId)
                        .fileName(fileName)
                        .fileParent(fileParent)
                        .filePart(filePart)
                        .fileSize(fileSize)
                        .build())
                .flatMap(fileService::uploadFile);
    }

    @Override
    @GetMapping("directories/{directoryId}/files/{fileId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<ResponseEntity<InputStreamResource>> downloadFile(
            @PathVariable String directoryId, @PathVariable String fileId, Authentication auth) {
        return fileService.userOwnsFile(auth.getName(), fileId)
                .map(userOwnsFile -> {
                    if (userOwnsFile) {
                        return fileId;
                    } else {
                        throw new UnauthorizedUserException(auth.getName());
                    }
                })
                .then()
                .flatMap(Void -> fileService.downloadFile(directoryId, fileId))
                .map(inputStream -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download-file\"")
                        .body(new InputStreamResource(inputStream)));
    }

    @Override
    @DeleteMapping("files/{fileId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<Void> deleteFile(@PathVariable String fileId, Authentication auth) {
        return fileService.userOwnsFile(auth.getName(), fileId)
                .map(userOwnsFile -> {
                    if (userOwnsFile) {
                        return fileId;
                    } else {
                        throw new UnauthorizedUserException(auth.getName());
                    }
                })
                .then()
                .flatMap(Void -> fileService.deleteFile(fileId));
    }
}
