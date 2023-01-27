package gongback.weeda.api.controller;

import gongback.weeda.common.base.ControllerTestSupport;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.service.ProfileService;
import gongback.weeda.service.type.FileType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import static gongback.weeda.common.TestProvider.EXAMPLE;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@WebFluxTest(FileController.class)
class FileControllerTest extends ControllerTestSupport {

    @MockBean
    ProfileService profileService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @WithMockUser
    @DisplayName("프로필 이미지 조회 성공(Controller)")
    void givenFileKey_thenSuccess() throws Exception {
        // given
        String fileKey = "profile/default-profile.png";
        String fileUrl = "https://test-temp-url.com/profile/default-profile.png";
        FileType fileType = FileType.PROFILE;

        when(profileService.getFileURL(fileKey))
                .thenReturn(Mono.just(fileUrl));

        // then
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/file/url")
                                .queryParam("fileKey", fileKey)
                                .queryParam("fileType", fileType)
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("code").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("msg").isEqualTo(ResponseCode.OK.getMsg())
                .jsonPath("data").isEqualTo(fileUrl)
                .consumeWith(getDocument(
                        requestParameters(
                            parameterWithName("fileKey").description("파일 키").attributes(field(EXAMPLE, fileKey)),
                            parameterWithName("fileType").description("파일 타입").attributes(field(EXAMPLE, fileType.name()))
                ),
                        responseFields(
                            fieldWithPath("code").description("응답 코드"),
                            fieldWithPath("msg").description("응답 메시지"),
                            fieldWithPath("data").description("이미지 URL")
                        ))
                )
                .consumeWith(System.out::println);
    }

}