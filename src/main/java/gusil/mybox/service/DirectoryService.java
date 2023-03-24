package gusil.mybox.service;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import reactor.core.publisher.Mono;

public interface DirectoryService {
    Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request);
    Mono<CreateDirectoryResponse> createRootDirectory(CreateDirectoryRequest request);
    Mono<ReadDirectoryItemListResponse> readDirectoryItemList(String directoryId, String userId, String userName);
    Mono<ReadDirectoryItemListResponse> readRootDirectoryItemList(String directoryId, String userId, String userName);
    Mono<Void> updateDirectoryOwner(String directoryId, String newOwnerId);
    Mono<Void> deleteDirectory(String directoryId);
    Mono<Boolean> directoryExists(String directoryId);
    Mono<Boolean> userOwnsDirectory(String username, String directory);
}
