package gongback.weeda.service;

import gongback.weeda.common.TestProvider;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.domain.role.entity.Role;
import gongback.weeda.domain.role.repository.RoleRepository;
import gongback.weeda.service.dto.RoleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    RoleService roleService;

    @Mock
    RoleRepository roleRepository;

    @Test
    @DisplayName("역할 저장 성공(RoleService)")
    void givenRoleDto_whenSaveRole_thenSuccess() throws Exception {
        // given
        Role testRole = TestProvider.createTestRole();
        RoleDto roleDto = DtoProvider.fromRole(testRole);

        // when
        when(roleRepository.save(any())).thenReturn(Mono.just(testRole));

        // then
        StepVerifier.create(roleService.save(roleDto))
                .verifyComplete();
    }

    @Test
    @DisplayName("역할 조회 성공(RoleService) - 역할 이름")
    void givenRoleName_whenSelectRole_thenSuccess() throws Exception {
        // given
        Role testRole = TestProvider.createTestRole();

        // when
        when(roleRepository.findByName(testRole.getName())).thenReturn(Mono.just(testRole));

        // then
        StepVerifier.create(roleService.findByName(testRole.getName()))
                .expectNextMatches(roleDto -> roleDto.name().equals(testRole.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("역할 조회 성공(RoleService) - 사용자 아이디")
    void givenUserId_whenSelectRole_thenSuccess() throws Exception {
        // given
        Long userId = new Random().nextLong(0, 100);
        Role testRole = TestProvider.createTestRole();

        // when
        when(roleRepository.findRoleNamesByUserId(any(Long.class))).thenReturn(Flux.just(testRole.getName()));

        // then
        StepVerifier.create(roleService.findRoleNamesByUserId(userId))
                .expectNextMatches(roleName -> roleName.equals(testRole.getName()))
                .verifyComplete();
    }

}