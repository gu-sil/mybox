package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import gusil.mybox.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users/{userId}/directories")
@RequiredArgsConstructor
public class DirectoryControllerImpl implements DirectoryController {
    private final DirectoryService directoryService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<CreateDirectoryResponse> createDirectory(
            @Valid @RequestBody CreateDirectoryRequest request, @PathVariable String userId) {
        return directoryService.createDirectory(request);
    }

    @Override
    @GetMapping("/root/items")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<ReadDirectoryItemListResponse> readRootDirectoryItemList(
            @PathVariable String userId, Authentication auth) {
        return directoryService.readRootDirectoryItemList("root", userId, auth.getName());
    }

    @Override
    @GetMapping("/{directoryId}/items")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<ReadDirectoryItemListResponse> readDirectoryItemList(
            @PathVariable String directoryId, @PathVariable String userId, Authentication auth) {
        return directoryService.readDirectoryItemList(directoryId, userId, auth.getName());
    }

    @Override
    @DeleteMapping("/{directoryId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<Void> deleteDirectory(
            @PathVariable String directoryId, @PathVariable String userId) {
        return directoryService.deleteDirectory(directoryId);
    }
}
