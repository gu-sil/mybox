package gusil.mybox.repository;

import gusil.mybox.domain.File;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FileRepository extends ReactiveMongoRepository<File, String> {
}
