package gongback.weeda.api.controller;

import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.TestProvider;
import gongback.weeda.common.base.RestDocsSupport;
import gongback.weeda.common.config.SecurityConfig;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.AuthService;
import gongback.weeda.utils.CreateDtoUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@Import(SecurityConfig.class)
@WebFluxTest(AuthController.class)
class AuthControllerTest extends RestDocsSupport {

    @MockBean
    AuthService authService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("회원가입 성공(Controller)")
    void givenAllInfo_thenSuccess() throws Exception {
        // given
        User testUser = TestProvider.createTestUser();
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("email", List.of(testUser.getEmail()));
        formData.put("password", List.of(testUser.getPassword()));
        formData.put("name", List.of(testUser.getName()));
        formData.put("nickname", List.of(testUser.getNickname()));
        formData.put("age", List.of(testUser.getAge().toString()));
        formData.put("gender", List.of(testUser.getGender()));
        SignUpRequest testSignUpRequest = TestProvider.createTestSignUpRequest(testUser);

        // when
        when(authService.signUp(CreateDtoUtil.fromRequest(testSignUpRequest, SocialType.WEEDA)))
                .thenReturn(Mono.just(CreateDtoUtil.fromUser(testUser)));

        // then
        webTestClient.post()
                .uri("/api/v1/auth/sign-up")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("code").isEqualTo(201)
                .consumeWith(getRequestParameterDocument(requestParameters(
                        parameterWithName("email").description("이메일").attributes(field("example", testUser.getEmail()), field("length", "0-20")),
                        parameterWithName("password").description("비밀번호").attributes(field("example", testUser.getPassword()), field("length", "10-15")),
                        parameterWithName("name").description("이름").attributes(field("example", testUser.getName())),
                        parameterWithName("nickname").description("닉네임").attributes(field("example", testUser.getNickname())),
                        parameterWithName("age").description("나이").attributes(field("example", String.valueOf(testUser.getAge()))),
                        parameterWithName("gender").description("성별").attributes(field("example", testUser.getGender()))
                )))
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("회원가입 실패(Controller) - 이메일 Validation 오류")
    void givenWrongEmail_thenFail() throws Exception {
        // given & when & then
        webTestClient.post()
                .uri("/api/v1/auth/sign-up")
                .body(BodyInserters.fromFormData("email", "test"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("code").isEqualTo(430)
                .jsonPath("data.errors[0].field").isEqualTo("email")
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("회원가입 실패(Controller) - 패스워드 Validation 오류")
    void givenWrongPassword_thenFail() throws Exception {
        // given & when & then
        webTestClient.post()
                .uri("/api/v1/auth/sign-up")
                .body(BodyInserters.fromFormData("password", "test1234"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("code").isEqualTo(430)
                .jsonPath("data.errors[0].field").isEqualTo("password")
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("회원가입 실패(Controller) - 이메일, 닉네임 동시 Validation 오류")
    void givenWrongEmailAndNickname_thenFail() throws Exception {
        // given
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("email", List.of("test"));
        multiValueMap.put("password", List.of("password"));

        // given & when & then
        webTestClient.post()
                .uri("/api/v1/auth/sign-up")
                .body(BodyInserters.fromFormData(multiValueMap))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("code").isEqualTo(430)
                .jsonPath("data.errors.size()").isEqualTo(2)
                .consumeWith(System.out::println);
    }

}