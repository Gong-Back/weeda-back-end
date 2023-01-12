package gongback.weeda.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(HttpStatus.OK.value(), "Success"),
    CREATED(HttpStatus.CREATED.value(), "Success Created"),
    DUPLICATED_ERROR(HttpStatus.CONFLICT.value(), "Duplicated error"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Requested Resource Is Not Found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Token is invalid"),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN.value(), "Permission is invalid"),
    ALREADY_LIKED(HttpStatus.CONFLICT.value(), "Member already liked the post"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "BadRequest"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"),

    REQUEST_VALIDATION_ERROR(430, "Validation error");

    private final int code;
    private final String msg;
}
