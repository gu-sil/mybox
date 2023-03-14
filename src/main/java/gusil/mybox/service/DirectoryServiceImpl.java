package gusil.mybox.service;

import gusil.mybox.dto.request.CreateDirectoryRequest;
import gusil.mybox.dto.response.CreateDirectoryResponse;
import gusil.mybox.mapper.DirectoryMapper;
import gusil.mybox.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {
    private final DirectoryRepository repository;
    private final DirectoryMapper mapper;

    @Override
    public Mono<CreateDirectoryResponse> createDirectory(CreateDirectoryRequest request) {
        return Mono
                .just(request)
                .map(mapper::mapToDirectory)
                .flatMap(repository::save)
                .map(mapper::mapToCreateDirectoryResponse);
    }
}
