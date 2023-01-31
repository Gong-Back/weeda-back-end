package gongback.weeda.api.controller.request;

import javax.validation.constraints.NotEmpty;

public record RoleRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
}
