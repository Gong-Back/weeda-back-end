package gongback.weeda.api.controller.request;

import javax.validation.constraints.NotEmpty;

public record EmailCheckRequest(
        @NotEmpty String email
) {
}
