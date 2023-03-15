package gusil.mybox.service;

import gusil.mybox.dto.request.UploadFileRequest;
import gusil.mybox.dto.response.UploadFileResponse;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<UploadFileResponse> uploadFile(UploadFileRequest request);
}
