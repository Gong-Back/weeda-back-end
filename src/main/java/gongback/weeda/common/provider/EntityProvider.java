package gongback.weeda.common.provider;

import gongback.weeda.api.controller.response.ApiResponse;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.domain.file.entity.Profile;
import gongback.weeda.domain.role.entity.Role;
import gongback.weeda.domain.role.entity.UserRole;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.ProfileDto;
import gongback.weeda.service.dto.RoleDto;
import gongback.weeda.service.dto.SignUpDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EntityProvider {
    public static User fromSignUpInfo(SignUpDto dto, String password, Long profileId) {
        return User.builder()
                .email(dto.email())
                .password(password)
                .name(dto.name())
                .nickname(dto.nickname())
                .age(dto.age())
                .gender(dto.gender())
                .socialType(dto.socialType().toString())
                .profileId(profileId)
                .build();
    }

    public static Profile fromProfileDto(final ProfileDto profileDto) {
        return Profile.builder()
                .fileKey(profileDto.fileKey())
                .serverFileName(profileDto.serverFileName())
                .originalFileName(profileDto.originalFileName())
                .build();
    }

    public static Role fromRoleDto(final RoleDto roleDto) {
        return Role.builder()
                .name(roleDto.name())
                .description(roleDto.description())
                .build();
    }

    public static UserRole fromUserRoleInfo(final Long userId, final Long roleId) {
        return UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
    }

    public static ResponseEntity ok() {
        return ResponseEntity.ok().body(ApiResponse.of(ResponseCode.OK));
    }

    public static ResponseEntity created() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(ResponseCode.CREATED));
    }

    public static ResponseEntity error(final ResponseCode responseCode) {
        HttpStatus httpStatus = HttpStatus.resolve(responseCode.getCode());
        return ResponseEntity.status(httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus)
                .body(ApiResponse.of(responseCode));
    }

    public static ResponseEntity error(final ResponseCode responseCode, final String errorMessage) {
        HttpStatus httpStatus = HttpStatus.resolve(responseCode.getCode());
        return ResponseEntity.status(httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus)
                .body(ApiResponse.of(responseCode, errorMessage));
    }

    public static ResponseEntity response(final ResponseCode responseCode) {
        HttpStatus httpStatus = HttpStatus.resolve(responseCode.getCode());
        return ResponseEntity.status(httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus)
                .body(ApiResponse.of(responseCode));
    }

    public static <T> ResponseEntity response(final ResponseCode responseCode, final T data) {
        HttpStatus resolve = HttpStatus.resolve(responseCode.getCode());
        if (resolve == null) {
            return ResponseEntity.badRequest().body(ApiResponse.of(responseCode, data));
        }

        return ResponseEntity.status(resolve).body(ApiResponse.of(responseCode, data));
    }
}
