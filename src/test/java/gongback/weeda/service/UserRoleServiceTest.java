package gongback.weeda.service;

import gongback.weeda.domain.role.entity.UserRole;
import gongback.weeda.domain.role.repository.UserRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

    @InjectMocks
    UserRoleService userRoleService;

    @Mock
    UserRoleRepository userRoleRepository;

    @Test
    @DisplayName("유저 역할 저장 성공(UserRoleService)")
    void givenUserIdAndRoleId_whenSaveUserRole_thenSuccess() throws Exception {
        // given
        Long userId = new Random().nextLong(0, 100);
        Long roleId = new Random().nextLong(0, 100);

        // when
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(Mono.empty());

        // then
        StepVerifier.create(userRoleService.save(userId, roleId))
                .verifyComplete();
    }

}