package gongback.weeda.api.controller;

import gongback.weeda.api.controller.request.EmailCheckRequest;
import gongback.weeda.api.controller.request.NicknameCheckRequest;
import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.service.AuthService;
import gongback.weeda.utils.CreateDtoUtil;
import gongback.weeda.utils.CreateEntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public Mono<ResponseEntity> signUp(@Valid SignUpRequest req) {
        return authService.signUp(CreateDtoUtil.fromRequest(req, SocialType.WEEDA))
                .thenReturn(CreateEntityUtil.created());
    }

    @PostMapping("/check-email")
    public Mono<ResponseEntity> checkEmail(@Valid EmailCheckRequest req) {
        return authService.checkEmail(req.email())
                .map(it -> {
                    checkDuplicatedResult(it);
                    return CreateEntityUtil.ok();
                });
    }

    @PostMapping("/check-nickname")
    public Mono<ResponseEntity> checkNickname(@Valid NicknameCheckRequest req) {
        return authService.checkNickname(req.nickname())
                .map(it -> {
                    checkDuplicatedResult(it);
                    return CreateEntityUtil.ok();
                });
    }

    private void checkDuplicatedResult(Boolean isDuplicated) {
        if (isDuplicated) {
            throw new WeedaApplicationException(ResponseCode.DUPLICATED_ERROR);
        }
    }
}
