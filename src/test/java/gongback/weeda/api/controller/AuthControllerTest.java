package gongback.weeda.api.controller;

import gongback.weeda.api.controller.request.DuplicateEmailRequest;
import gongback.weeda.api.controller.request.DuplicateNicknameRequest;
import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.TestProvider;
import gongback.weeda.common.base.RestDocsSupport;
import gongback.weeda.common.config.SecurityConfig;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        when(authService.signUp(DtoProvider.fromRequest(testSignUpRequest, SocialType.WEEDA)))
                .thenReturn(Mono.just(DtoProvider.fromUser(testUser)));

        // then
        webTestClient.post()
                .uri("/api/v1/auth/sign-up")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("code").isEqualTo(201)
                .consumeWith(getDocument(requestParameters(
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
        User testUser = TestProvider.createTestUser();
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        String email = "test";
        String password = "password";
        formData.put("email", List.of(email));
        formData.put("password", List.of(password));
        formData.put("name", List.of(testUser.getName()));
        formData.put("nickname", List.of(testUser.getNickname()));
        formData.put("age", List.of(testUser.getAge().toString()));
        formData.put("gender", List.of(testUser.getGender()));

        // given & when & then
        ResponseFieldsSnippet responseFieldsSnippet = responseFields();
        webTestClient.post()
                .uri("/api/v1/auth/sign-up")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("code").isEqualTo(430)
                .jsonPath("data.errors.size()").isEqualTo(2)
                .consumeWith(getDocument(
                        requestParameters(
                                parameterWithName("email").description("잘못된 이메일 형식").attributes(field("example", email), field("length", "0-20")),
                                parameterWithName("password").description("잘못된 비밀번호 형식").attributes(field("example", password), field("length", "10-15")),
                                parameterWithName("name").description("이름").attributes(field("example", testUser.getName())),
                                parameterWithName("nickname").description("닉네임").attributes(field("example", testUser.getNickname())),
                                parameterWithName("age").description("나이").attributes(field("example", String.valueOf(testUser.getAge()))),
                                parameterWithName("gender").description("성별").attributes(field("example", testUser.getGender()))
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지"),
                                fieldWithPath("data.errors").description("검증 에러 목록"),
                                fieldWithPath("data.errors[0].field").description("검증 에러 항목"),
                                fieldWithPath("data.errors[0].msg").description("검증 에러 메시지")
                        )))
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("이메일 중복 체크 - 중복 X(Controller)")
    void givenNotDuplicatedEmail_thenSuccess() throws Exception {
        // given
        DuplicateEmailRequest request = new DuplicateEmailRequest("test@test.com");

        // when
        when(authService.checkEmail(request.email())).thenReturn(Mono.just(false));

        // then
        webTestClient.post()
                .uri("/api/v1/auth/check-email")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("code").isEqualTo(HttpStatus.OK.value())
                .consumeWith(getDocument(
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지")
                        )))
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("이메일 중복 체크 - 중복 O(Controller)")
    void givenDuplicatedEmail_thenFail() throws Exception {
        // given
        DuplicateEmailRequest request = new DuplicateEmailRequest("test@test.com");

        // when
        when(authService.checkEmail(request.email())).thenThrow(new WeedaApplicationException(ResponseCode.DUPLICATED_ERROR));

        // then
        webTestClient.post()
                .uri("/api/v1/auth/check-email")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("code").isEqualTo(ResponseCode.DUPLICATED_ERROR.getCode())
                .consumeWith(getDocument(
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지")
                        )))
                .consumeWith(System.out::println);
    }


    @Test
    @DisplayName("이메일 중복 체크 - NULL 값(Controller)")
    void givenNullEmail_thenFail() throws Exception {
        // given
        DuplicateEmailRequest request = new DuplicateEmailRequest(null);

        // when & then
        webTestClient.post()
                .uri("/api/v1/auth/check-email")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("code").isEqualTo(ResponseCode.REQUEST_VALIDATION_ERROR.getCode())
                .consumeWith(getDocument(
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지"),
                                subsectionWithPath("data").description("오류 데이터")
                        )))
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 중복 X(Controller)")
    void givenNotDuplicatedNickname_thenSuccess() throws Exception {
        // given
        DuplicateNicknameRequest request = new DuplicateNicknameRequest("testUser");

        // when
        when(authService.checkNickname(request.nickname())).thenReturn(Mono.just(false));

        // then
        webTestClient.post()
                .uri("/api/v1/auth/check-nickname")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("code").isEqualTo(HttpStatus.OK.value())
                .consumeWith(getDocument(
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지")
                        )))
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 중복 O(Controller)")
    void givenDuplicatedNickname_thenFail() throws Exception {
        // given
        DuplicateNicknameRequest request = new DuplicateNicknameRequest("testUser");

        // when
        when(authService.checkNickname(request.nickname())).thenThrow(new WeedaApplicationException(ResponseCode.DUPLICATED_ERROR));

        // then
        webTestClient.post()
                .uri("/api/v1/auth/check-nickname")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("code").isEqualTo(ResponseCode.DUPLICATED_ERROR.getCode())
                .consumeWith(getDocument(
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지")
                        )))
                .consumeWith(System.out::println);
    }


    @Test
    @DisplayName("닉네임 중복 체크 - NULL 값(Controller)")
    void givenNullNickname_thenFail() throws Exception {
        // given
        DuplicateNicknameRequest request = new DuplicateNicknameRequest(null);

        // when & then
        webTestClient.post()
                .uri("/api/v1/auth/check-nickname")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("code").isEqualTo(ResponseCode.REQUEST_VALIDATION_ERROR.getCode())
                .consumeWith(getDocument(
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("msg").description("응답 메시지"),
                                subsectionWithPath("data").description("오류 데이터")
                        )))
                .consumeWith(System.out::println);
    }

}