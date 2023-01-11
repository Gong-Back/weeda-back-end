package gongback.weeda.api.controller.request;

import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record SignUpRequest(

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,
        @Size(min = 10, max = 15, message = "비밀번호는 10-15 사이여야 합니다.")
        String password,
        String name,
        String nickname,
        String gender,
        Integer age,
        FilePart profile
) {
}
