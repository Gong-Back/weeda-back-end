package gongback.weeda.domain.role.repository;

import gongback.weeda.domain.role.entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
    Mono<Role> findByName(final String name);

    @Query("select name from role inner join user_role on user_role.user_id = :userId and role.id = user_role.role_id")
    Flux<String> findRoleNamesByUserId(final long userId);
}
