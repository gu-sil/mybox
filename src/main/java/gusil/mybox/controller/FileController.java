package gusil.mybox.controller;

import gusil.mybox.dto.response.UploadFileResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public interface FileController {
    Mono<UploadFileResponse> uploadFile(
            String userId,
            String fileName,
            String fileParent,
            Mono<FilePart> filePart,
            Long fileSize,
            Authentication auth
    );

    Mono<ResponseEntity<InputStreamResource>> downloadFile(String directoryId, String fileId, Authentication auth);

    Mono<Void> deleteFile(String fileId, Authentication auth);
}
