package gongback.weeda.service;

import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.domain.role.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public Mono<Void> save(final Long userId, final Long roleId) {
        return userRoleRepository.save(EntityProvider.fromUserRoleInfo(userId, roleId))
                .then();
    }
}
