package gongback.weeda.api.controller;

import gongback.weeda.api.controller.request.DuplicateEmailRequest;
import gongback.weeda.api.controller.request.DuplicateNicknameRequest;
import gongback.weeda.api.controller.request.SignInRequest;
import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.service.AuthService;
import gongback.weeda.service.dto.JwtDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
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

    @PostMapping("/sign-in")
    public Mono<ResponseEntity> signIn(@RequestBody @Valid SignInRequest req, ServerWebExchange exchange) {
        return authService.signIn(req.email(), req.password())
                .flatMap(it -> {
                    addTokenToHeader(exchange, it);
                    return Mono.just(EntityProvider.ok());
                });
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

    private void addTokenToHeader(ServerWebExchange exchange, JwtDto it) {
        exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, it.token());
    }
}
