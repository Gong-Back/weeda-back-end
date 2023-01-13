package gongback.weeda.api.controller.request;

import javax.validation.constraints.NotEmpty;

public record DuplicateNicknameRequest(
        @NotEmpty String nickname
) {
}
