package gusil.mybox.repository;

import gusil.mybox.domain.Directory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DirectoryRepository extends ReactiveMongoRepository<Directory, String> {
}
