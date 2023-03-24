package gusil.mybox.repository;

import gusil.mybox.domain.File;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends ReactiveMongoRepository<File, String> {
    Flux<File> findAllByFileParent(String fileParentId);
    Flux<File> findAllByFileParentAndFileOwner(String fileParentId, String fileOwner);
    Mono<Boolean> existsByFileIdAndFileParent(String fileId, String fileParent);
    Mono<Boolean> existsByFileNameAndFileParent(String fileName, String fileParent);
    Mono<Boolean> existsByFileParent(String fileParent);
}
