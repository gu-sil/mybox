package gusil.mybox.service;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import reactor.core.publisher.Mono;

public interface DirectoryService {
    Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request);
}
