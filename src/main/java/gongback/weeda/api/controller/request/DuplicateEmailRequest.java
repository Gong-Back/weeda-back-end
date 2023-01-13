package gongback.weeda.api.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public record DuplicateEmailRequest(
        @NotEmpty @Email String email
) {
}
