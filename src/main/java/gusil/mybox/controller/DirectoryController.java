package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public interface DirectoryController {
    Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request, String userId);
    Mono<ReadDirectoryItemListResponse> readDirectoryItemList(String directoryId, String userId, Authentication auth);
    Mono<Void> deleteDirectory(String directoryId, String userId);
}
