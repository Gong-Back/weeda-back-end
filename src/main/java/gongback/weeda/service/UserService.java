package gongback.weeda.service;

import gongback.weeda.domain.user.entity.User;
import gongback.weeda.domain.user.repository.UserRepository;
import gongback.weeda.service.dto.UserResDto;
import gongback.weeda.utils.CreateDtoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserResDto> saveUser(User user) {
        return userRepository.save(user)
                .map(CreateDtoUtil::fromUser);
    }

    public Mono<Boolean> checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<Boolean> checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
