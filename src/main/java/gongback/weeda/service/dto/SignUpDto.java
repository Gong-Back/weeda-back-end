package gongback.weeda.service.dto;

import gongback.weeda.common.type.SocialType;
import org.springframework.http.codec.multipart.FilePart;

public record SignUpDto(
        String email,
        String password,
        String name,
        String nickname,
        String gender,
        Integer age,
        FilePart profile,
        SocialType socialType
) {
}
