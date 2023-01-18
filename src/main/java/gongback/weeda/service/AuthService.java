package gongback.weeda.service;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.jwt.JwtSupport;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.service.dto.JwtDto;
import gongback.weeda.service.dto.SignUpDto;
import gongback.weeda.service.dto.UserResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtSupport jwtSupport;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public Mono<UserResDto> signUp(SignUpDto signUpDto) {
        return userService.saveUser(EntityProvider.fromSignUpInfo(
                signUpDto,
                passwordEncoder.encode(signUpDto.password()))
        );
    }

    public Mono<JwtDto> signIn(String email, String password) {
        return userService.findByEmail(email)
                .filter(it -> isValidPassword(password, it.Password()))
                .switchIfEmpty(Mono.error(new WeedaApplicationException(ResponseCode.INVALID_PASSWORD)))
                .map(it -> DtoProvider.fromBearerToken(jwtSupport.generateJwt(email, null)));
    }

    private boolean isValidPassword(String password, String savedPassword) {
        return passwordEncoder.matches(password, savedPassword);
    }

    public Mono<Boolean> checkEmail(String email) {
        return userService.existsByEmail(email)
                .map(it -> checkDuplicatedResult(it));
    }

    public Mono<Boolean> checkNickname(String nickname) {
        return userService.existsByNickname(nickname)
                .map(it -> checkDuplicatedResult(it));
    }

    private boolean checkDuplicatedResult(Boolean isDuplicated) {
        if (isDuplicated) {
            throw new WeedaApplicationException(ResponseCode.DUPLICATED_ERROR);
        }

        return false;
    }
}
