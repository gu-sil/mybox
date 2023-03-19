package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import reactor.core.publisher.Mono;

public interface DirectoryController {
    Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request);
    Mono<ReadDirectoryItemListResponse> readDirectoryItemList(String directoryId);
    Mono<Void> deleteDirectory(String directoryId);
}
