package gongback.weeda.api.controller.request;

import javax.validation.constraints.NotEmpty;

public record NicknameCheckRequest(
        @NotEmpty String nickname
) {
}
