package gongback.weeda.util;

import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;

public class CreateUtil {

    public static User createUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .socialType(SocialType.WEEDA.toString())
                .build();
    }
}
