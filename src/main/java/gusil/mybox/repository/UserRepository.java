package gusil.mybox.repository;

import gusil.mybox.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
