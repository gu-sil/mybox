package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import reactor.core.publisher.Mono;

public interface DirectoryController {
    Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request);
}
