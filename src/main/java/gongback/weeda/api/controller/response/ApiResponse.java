package gongback.weeda.api.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import gongback.weeda.common.exception.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;

    public static ApiResponse of(ResponseCode responseCode) {
        return ApiResponse.of(responseCode.getCode(), responseCode.getMsg(), null);
    }

    public static ApiResponse of(ResponseCode responseCode, String message) {
        return ApiResponse.of(responseCode.getCode(), message, null);
    }

    public static <T> ApiResponse<T> of(ResponseCode responseCode, T data) {
        return ApiResponse.of(responseCode.getCode(), responseCode.getMsg(), data);
    }
}
