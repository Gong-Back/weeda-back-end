package gongback.weeda.service;

import gongback.weeda.common.TestProvider;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("검증 성공(CustomUserDetailsService)")
    void givenAllInfo_whenCheckUser_thenSuccess() throws Exception {
        // given
        User testUser = TestProvider.createTestUser();

        // when
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.just(testUser));

        // then
        StepVerifier.create(customUserDetailsService.findByUsername(testUser.getEmail()))
                .expectNextMatches(it -> it.getUsername().equals(testUser.getEmail()));
    }

}