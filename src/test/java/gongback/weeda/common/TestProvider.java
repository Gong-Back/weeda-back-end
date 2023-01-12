package gongback.weeda.common;

import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;

import java.util.Random;

public class TestProvider {
    public static User createTestUser() {
        int randomValue = new Random().nextInt();
        return User.builder()
                .email("test" + Math.abs(randomValue) + "@test.com")
                .password("test123456")
                .name("testUser")
                .nickname("NK" + randomValue)
                .age(20)
                .gender("m")
                .socialType(SocialType.WEEDA.toString())
                .build();
    }

    public static SignUpRequest createTestSignUpRequest(User user) {
        return new SignUpRequest(user.getEmail(), user.getPassword(), user.getName(), user.getNickname(),
                user.getGender(), user.getAge(), null);
    }
}
