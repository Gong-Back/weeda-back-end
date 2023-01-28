package gongback.weeda.common;

import gongback.weeda.api.controller.request.SignInRequest;
import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.file.entity.Profile;
import gongback.weeda.domain.role.entity.Role;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.JwtDto;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Random;

public class TestProvider {

    public static final String EXAMPLE = "example";
    public static final String LENGTH = "length";

    public static User createTestUser() {
        int randomValue = new Random().nextInt();
        return User.builder()
                .email("test" + Math.abs(randomValue) + "@test.com")
                .password("test123456")
                .name("testUser")
                .nickname("NK" + randomValue)
                .age(20)
                .gender("m")
                .profileId(1L)
                .socialType(SocialType.WEEDA.toString())
                .build();
    }

    public static User createProfileTestUser() {
        int randomValue = new Random().nextInt();
        return User.builder()
                .email("test" + Math.abs(randomValue) + "@test.com")
                .password("test123456")
                .name("testUser")
                .nickname("NK" + randomValue)
                .age(20)
                .gender("m")
                .profileId(2L)
                .socialType(SocialType.WEEDA.toString())
                .build();
    }

    public static Role createTestRole() {
        return Role.builder()
                .name("ROLE_USER")
                .description("TEST ROLE")
                .build();
    }

    public static SignUpRequest createTestSignUpRequest(User user) {
        return new SignUpRequest(user.getEmail(), user.getPassword(), user.getName(), user.getNickname(),
                user.getGender(), user.getAge(), null);
    }

    public static SignUpRequest createTestSignUpWithProfileRequest(User user, FilePart profile) {
        return new SignUpRequest(user.getEmail(), user.getPassword(), user.getName(), user.getNickname(),
                user.getGender(), user.getAge(), profile);
    }

    public static SignInRequest createTestSignInRequest(User user) {
        return new SignInRequest(user.getEmail(), user.getPassword());
    }

    public static JwtDto createJwtDto(String token) {
        return new JwtDto(token);
    }

    public static Profile createTestProfile() {
        return Profile.builder()
                .originalFileName("test-image.jpeg")
                .serverFileName("server-save-test-image.jpeg")
                .fileKey("profile/server-save-test-image.jpeg")
                .build();
    }
}
