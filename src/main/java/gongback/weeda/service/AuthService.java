package gongback.weeda.service;

import gongback.weeda.service.dto.SignUpDto;
import gongback.weeda.service.dto.UserResDto;
import gongback.weeda.utils.CreateEntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public Mono<UserResDto> signUp(SignUpDto signUpDto) {
        return userService.saveUser(CreateEntityUtil.fromSignUpInfo(
                signUpDto,
                passwordEncoder.encode(signUpDto.password()))
        );
    }

    public Mono<Boolean> checkEmail(String email) {
        return userService.checkEmail(email);
    }

    public Mono<Boolean> checkNickname(String nickname) {
        return userService.checkNickname(nickname);
    }
}
