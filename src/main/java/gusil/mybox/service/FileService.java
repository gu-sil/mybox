package gusil.mybox.service;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import reactor.core.publisher.Mono;
import java.io.InputStream;

public interface FileService {
    Mono<UploadFileResponse> uploadFile(UploadFileRequest request);

    Mono<InputStream> downloadFile(String directoryId, String fileId);

    Mono<Void> deleteFile(String fileId);
}
