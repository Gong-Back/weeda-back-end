package gongback.weeda.domain.user.repository;

import gongback.weeda.domain.user.entity.User;
import gongback.weeda.util.CreateUtil;
import gongback.weeda.util.base.RepositoryUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends RepositoryUnitTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void saveUser() throws Exception {
        // given
        User user = CreateUtil.createUser("test@test.com", "test1234");

        // when & then
        transactionStepVerifier.create(userRepository.save(user))
                .expectNextMatches(it -> it.getEmail() == user.getEmail())
                .verifyComplete();
    }

}