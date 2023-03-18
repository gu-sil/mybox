package gusil.mybox.repository;

import gusil.mybox.domain.File;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends ReactiveMongoRepository<File, String> {
    Flux<File> findAllByFileParent(String fileParentId);

    Mono<Boolean> existsByFileIdAndFileParent(String fileId, String fileParent);
}
