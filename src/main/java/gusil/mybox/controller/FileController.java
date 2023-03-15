package gusil.mybox.controller;

import gusil.mybox.dto.response.UploadFileResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileController {
    Mono<UploadFileResponse> uploadFile(
            String userId,
            String fileName,
            String fileParent,
            Mono<FilePart> filePart,
            Long fileSize
    );
}
