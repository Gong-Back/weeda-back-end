package gongback.weeda.domain.user.repository;

import gongback.weeda.common.TestProvider;
import gongback.weeda.common.base.RepositoryUnitTest;
import gongback.weeda.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends RepositoryUnitTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공(UserRepository)")
    void saveUser() throws Exception {
        // given
        User user = TestProvider.createTestUser();

        // when & then
        transactionStepVerifier.create(userRepository.save(user))
                .expectNextMatches(it -> it.getEmail() == user.getEmail())
                .verifyComplete();
    }

    @Test
    @DisplayName("이메일 체크 중복(UserRepository)")
    void checkEmail() throws Exception {
        // given
        User user = TestProvider.createTestUser();

        // when & then
        transactionStepVerifier.create(
                        userRepository.save(user)
                                .flatMap(it -> userRepository.existsByEmail(it.getEmail()))
                )
                .expectNextMatches(it -> it == true)
                .verifyComplete();
    }

    @Test
    @DisplayName("닉네임 체크 중복(UserRepository)")
    void checkNickname() throws Exception {
        // given
        User user = TestProvider.createTestUser();

        // when & then
        transactionStepVerifier.create(
                        userRepository.save(user)
                                .flatMap(it -> userRepository.existsByNickname(it.getNickname()))
                )
                .expectNextMatches(it -> it == true)
                .verifyComplete();
    }

}