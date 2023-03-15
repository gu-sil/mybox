package gusil.mybox.controller;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileController {
    Mono<Void> uploadFile(
            String userId,
            String fileName,
            String fileParent,
            Mono<FilePart> filePart,
            Long fileSize
    ) throws InterruptedException;
}
