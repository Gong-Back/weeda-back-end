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
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공(UserService)")
    void givenAllInfo_thenSuccess() throws Exception {
        // given
        User testUser = TestProvider.createTestUser();

        // when
        when(userRepository.save(testUser)).thenReturn(Mono.just(testUser));

        // then
        StepVerifier.create(userService.saveUser(testUser))
                .expectNextMatches(it -> it.email() == testUser.getEmail())
                .verifyComplete();
    }

    @Test
    @DisplayName("이메일 중복 체크 성공(UserService)")
    void givenEmail_whenNotExistsEmail_thenSuccess() throws Exception {
        // given
        String email = "test@test.com";

        // when
        when(userRepository.existsByEmail(email)).thenReturn(Mono.just(true));

        // then
        StepVerifier.create(userService.existsByEmail(email))
                .expectNextMatches(it -> it == true)
                .verifyComplete();
    }

    @Test
    @DisplayName("이메일 중복 체크 실패 - 중복 값(UserService)")
    void givenEmail_whenExistsEmail_thenFail() throws Exception {
        // given
        String email = "test@test.com";

        // when
        when(userRepository.existsByEmail(email)).thenReturn(Mono.just(false));

        // then
        StepVerifier.create(userService.existsByEmail(email))
                .expectNextMatches(it -> it == false)
                .verifyComplete();
    }

    @Test
    @DisplayName("닉네임 중복 체크 성공(UserService)")
    void givenNickname_whenNotExistsNickname_thenSuccess() throws Exception {
        // given
        String nickname = "testUser";

        // when
        when(userRepository.existsByNickname(nickname)).thenReturn(Mono.just(true));

        // then
        StepVerifier.create(userService.existsByNickname(nickname))
                .expectNextMatches(it -> it == true)
                .verifyComplete();
    }

    @Test
    @DisplayName("닉네임 중복 체크 실패 - 중복 값(UserService)")
    void givenNickname_whenExistsNickname_thenFail() throws Exception {
        // given
        String nickname = "testUser";

        // when
        when(userRepository.existsByNickname(nickname)).thenReturn(Mono.just(false));

        // then
        StepVerifier.create(userService.existsByNickname(nickname))
                .expectNextMatches(it -> it == false)
                .verifyComplete();
    }

    @Test
    @DisplayName("유저 찾기 성공(UserService)")
    void givenEmail_whenFindUserByEmail_thenSuccess() throws Exception {
        // given
        User testUser = TestProvider.createTestUser();

        // when
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.just(testUser));

        // then
        StepVerifier.create(userService.findByEmail(testUser.getEmail()))
                .expectNextMatches(it -> it.email().equals(testUser.getEmail()))
                .verifyComplete();
    }
}