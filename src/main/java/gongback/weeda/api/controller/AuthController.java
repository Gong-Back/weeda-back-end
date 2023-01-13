package gongback.weeda.api.controller;

import gongback.weeda.api.controller.request.DuplicateEmailRequest;
import gongback.weeda.api.controller.request.DuplicateNicknameRequest;
import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        return authService.signUp(DtoProvider.fromRequest(req, SocialType.WEEDA))
                .thenReturn(EntityProvider.created());
    }

    @PostMapping("/check-email")
    public Mono<ResponseEntity> checkEmail(@Valid @RequestBody DuplicateEmailRequest req) {
        return authService.checkEmail(req.email())
                .thenReturn(EntityProvider.ok());
    }

    @PostMapping("/check-nickname")
    public Mono<ResponseEntity> checkNickname(@Valid @RequestBody DuplicateNicknameRequest req) {
        return authService.checkNickname(req.nickname())
                .thenReturn(EntityProvider.ok());
    }
}
