package gusil.mybox.controller;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/directories")
@RequiredArgsConstructor
public class DirectoryControllerImpl implements DirectoryController {
    private final DirectoryService directoryService;

    @Override
    @PostMapping
    public Mono<CreateDirectoryResponse> createDirectory(@Valid @RequestBody CreateDirectoryRequest request) {
        return directoryService.createDirectory(request);
    }
}
