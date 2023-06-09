package gusil.mybox.repository;

import gusil.mybox.domain.Directory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectoryRepository extends ReactiveMongoRepository<Directory, String> {
    Flux<Directory> findAllByDirectoryParent(String directoryOwner);
    Flux<Directory> findAllByDirectoryParentAndDirectoryOwner(String directoryParent, String directoryOwner);
    Mono<Boolean> existsByDirectoryParent(String directoryParent);
    Mono<Boolean> existsByDirectoryNameAndDirectoryParent(String directoryName, String directoryParent);
}
