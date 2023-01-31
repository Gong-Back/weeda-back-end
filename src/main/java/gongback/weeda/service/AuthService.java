package gongback.weeda.service;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.jwt.JwtSupport;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.service.dto.JwtDto;
import gongback.weeda.service.dto.SignUpDto;
import gongback.weeda.service.type.FileType;
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
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final ProfileService profileService;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final String BASIC_ROLE = "ROLE_USER";

    public Mono<Void> signUp(SignUpDto signUpDto) {
        if (signUpDto.profile() == null) {
            return userService.saveUser(EntityProvider.fromSignUpInfo(
                            signUpDto,
                            passwordEncoder.encode(signUpDto.password()),
                            1L))
                    .zipWith(roleService.findByName(BASIC_ROLE))
                    .flatMap(it -> userRoleService.save(it.getT1().id(), it.getT2().id()))
                    .then();
        }

        return profileService.uploadProfile(signUpDto.profile(), FileType.PROFILE)
                .flatMap(profileId -> userService.saveUser(
                        EntityProvider.fromSignUpInfo(
                                signUpDto,
                                passwordEncoder.encode(signUpDto.password()),
                                profileId)
                ))
                .zipWith(roleService.findByName(BASIC_ROLE))
                .flatMap(it -> userRoleService.save(it.getT1().id(), it.getT2().id()))
                .then();
    }

    public Mono<JwtDto> signIn(String email, String password) {
        return userService.findByEmail(email)
                .filter(userResDto -> isValidPassword(password, userResDto.Password()))
                .switchIfEmpty(Mono.error(new WeedaApplicationException(ResponseCode.INVALID_PASSWORD)))
                .map(roleNameList -> DtoProvider.fromBearerToken(jwtSupport.generateJwt(email)));
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
