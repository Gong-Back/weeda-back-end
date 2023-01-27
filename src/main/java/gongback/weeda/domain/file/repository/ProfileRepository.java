package gongback.weeda.domain.file.repository;

import gongback.weeda.domain.file.entity.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long> {
    Mono<Boolean> existsByFileKey(String fileKey);
}
