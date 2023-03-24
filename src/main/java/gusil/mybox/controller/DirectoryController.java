package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface DirectoryController {
    Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request, String userId);

    @GetMapping("/root/items")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    Mono<ReadDirectoryItemListResponse> readRootDirectoryItemList(
            @PathVariable String userId, Authentication auth);

    Mono<ReadDirectoryItemListResponse> readDirectoryItemList(String directoryId, String userId, Authentication auth);

    Mono<Void> deleteDirectory(String directoryId, String userId);
}
