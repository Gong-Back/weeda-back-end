package gongback.weeda.domain.role.repository;

import gongback.weeda.domain.role.entity.UserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Long> {
}
