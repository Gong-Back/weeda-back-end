package gongback.weeda.service;

import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.domain.role.repository.RoleRepository;
import gongback.weeda.service.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Mono<Void> save(final RoleDto roleDto) {
        return roleRepository.save(EntityProvider.fromRoleDto(roleDto))
                .then();
    }

    public Mono<RoleDto> findByName(final String name) {
        return roleRepository.findByName(name)
                .map(it -> DtoProvider.fromRole(it));
    }

    public Flux<String> findRoleNamesByUserId(final long userId) {
        return roleRepository.findRoleNamesByUserId(userId);
    }
}
