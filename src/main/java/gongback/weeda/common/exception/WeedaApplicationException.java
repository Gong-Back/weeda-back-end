package gongback.weeda.common.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class WeedaApplicationException extends RuntimeException {
    private ResponseCode responseCode;
    private String msg;

    public WeedaApplicationException(ResponseCode responseCode) {
        this.responseCode = responseCode;
        this.msg = null;
    }

    public WeedaApplicationException(ResponseCode responseCode, String msg) {
        this.responseCode = responseCode;
        this.msg = msg;
    }

    public String getMsg() {
        if (!StringUtils.hasText(msg)) {
            return responseCode.getMsg();
        }

        return String.format("%s, %s", responseCode.getMsg(), msg);
    }
}
