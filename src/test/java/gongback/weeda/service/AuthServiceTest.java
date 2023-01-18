package gongback.weeda.service;

import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.jwt.BearerToken;
import gongback.weeda.common.jwt.JwtSupport;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.SignUpDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static gongback.weeda.common.TestProvider.createTestSignUpRequest;
import static gongback.weeda.common.TestProvider.createTestUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    JwtSupport jwtSupport;

    @Mock
    UserService userService;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공(AuthService)")
    void givenAllInfo_thenSuccess() throws Exception {
        // given
        User testUser = createTestUser();
        SignUpRequest testSignUpRequest = createTestSignUpRequest(testUser);
        SignUpDto testSignUpDto = DtoProvider.fromRequest(testSignUpRequest, SocialType.WEEDA);
        String encodedPassword = "testEncodedPassword";

        // when
        when(passwordEncoder.encode(testSignUpDto.password())).thenReturn(encodedPassword);
        when(userService.saveUser(any())).thenReturn(Mono.just(DtoProvider.fromUser(testUser)));

        // then
        StepVerifier.create(authService.signUp(testSignUpDto))
                .expectNextMatches(it -> it.email() == testUser.getEmail())
                .verifyComplete();
    }

    @Test
    @DisplayName("이메일 중복 체크 성공(AuthService)")
    void givenEmail_whenNotExistsEmail_thenSuccess() throws Exception {
        // given
        String email = "test@test.com";

        // when
        when(userService.existsByEmail(email)).thenReturn(Mono.just(true));

        // then
        StepVerifier.create(authService.checkEmail(email))
                .expectError(WeedaApplicationException.class)
                .verify();
    }

    @Test
    @DisplayName("이메일 중복 체크 실패 - 중복 값(AuthService)")
    void givenEmail_whenExistsEmail_thenFail() throws Exception {
        // given
        String email = "test@test.com";

        // when
        when(userService.existsByEmail(email)).thenReturn(Mono.just(false));

        // then
        StepVerifier.create(authService.checkEmail(email))
                .expectNextMatches(it -> it == false)
                .verifyComplete();
    }

    @Test
    @DisplayName("닉네임 중복 체크 성공(AuthService)")
    void givenNickname_whenNotExistsNickname_thenSuccess() throws Exception {
        // given
        String nickname = "testUser";

        // when
        when(userService.existsByNickname(nickname)).thenReturn(Mono.just(true));

        // then
        StepVerifier.create(authService.checkNickname(nickname))
                .expectError(WeedaApplicationException.class)
                .verify();
    }

    @Test
    @DisplayName("닉네임 중복 체크 실패 - 중복 값(AuthService)")
    void givenNickname_whenExistsNickname_thenFail() throws Exception {
        // given
        String nickname = "testUser";

        // when
        when(userService.existsByNickname(nickname)).thenReturn(Mono.just(false));

        // then
        StepVerifier.create(authService.checkNickname(nickname))
                .expectNextMatches(it -> it == false)
                .verifyComplete();
    }

    @Test
    @DisplayName("로그인 성공(AuthService)")
    void givenAllInfo_whenSignIn_thenSuccess() throws Exception {
        // given
        User testUser = createTestUser();
        String token = "testToken";

        // when
        when(userService.findByEmail(testUser.getEmail())).thenReturn(Mono.just(DtoProvider.fromUser(testUser)));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtSupport.generateJwt(testUser.getEmail(), null)).thenReturn(new BearerToken(null, token));

        // then
        StepVerifier.create(authService.signIn(testUser.getEmail(), testUser.getPassword()))
                .expectNextMatches(it -> it.token() == token)
                .verifyComplete();
    }
}