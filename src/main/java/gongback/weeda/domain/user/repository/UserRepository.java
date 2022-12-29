package gongback.weeda.domain.user.repository;

import gongback.weeda.domain.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
