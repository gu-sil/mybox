package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.dto.response.ReadDirectoryItemListResponse;
import gusil.mybox.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/directories")
@RequiredArgsConstructor
public class DirectoryControllerImpl implements DirectoryController {
    private final DirectoryService directoryService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<CreateDirectoryResponse> createDirectory(@Valid @RequestBody CreateDirectoryRequest request) {
        return directoryService.createDirectory(request);
    }

    @Override
    @GetMapping("/{directoryId}/items")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<ReadDirectoryItemListResponse> readDirectoryItemList(@PathVariable String directoryId) {
        return directoryService.readDirectoryItemList(directoryId);
    }

    @Override
    @DeleteMapping("/{directoryId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Mono<Void> deleteDirectory(@PathVariable String directoryId) {
        return directoryService.deleteDirectory(directoryId);
    }
}
