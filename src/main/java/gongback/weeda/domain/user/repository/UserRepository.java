package gongback.weeda.domain.user.repository;

import gongback.weeda.domain.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByNickname(String nickname);

    Mono<User> findByEmail(String email);
}
