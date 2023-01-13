package gongback.weeda.common.provider;

import gongback.weeda.api.controller.response.ApiResponse;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.SignUpDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EntityProvider {
    public static User fromSignUpInfo(SignUpDto dto, String password) {
        return User.builder()
                .email(dto.email())
                .password(password)
                .name(dto.name())
                .nickname(dto.nickname())
                .age(dto.age())
                .gender(dto.gender())
                .profileUrl(null)
                .socialType(dto.socialType().toString())
                .build();
    }

    public static ResponseEntity ok() {
        return ResponseEntity.ok().body(ApiResponse.of(ResponseCode.OK));
    }

    public static ResponseEntity created() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(ResponseCode.CREATED));
    }

    public static ResponseEntity response(ResponseCode responseCode) {
        return ResponseEntity.status(HttpStatus.resolve(responseCode.getCode())).body(ApiResponse.of(responseCode));
    }

    public static <T> ResponseEntity response(ResponseCode responseCode, T data) {
        HttpStatus resolve = HttpStatus.resolve(responseCode.getCode());
        if (resolve == null) {
            return ResponseEntity.badRequest().body(ApiResponse.of(responseCode, data));
        }

        return ResponseEntity.status(resolve).body(ApiResponse.of(responseCode, data));
    }
}
