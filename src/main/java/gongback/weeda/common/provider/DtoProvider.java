package gongback.weeda.common.provider;

import gongback.weeda.api.controller.request.RoleRequest;
import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.jwt.BearerToken;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.role.entity.Role;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.*;

public class DtoProvider {
    public static SignUpDto fromRequest(final SignUpRequest req, final SocialType socialType) {
        return new SignUpDto(req.email(), req.password(), req.name(), req.nickname(), req.gender(),
                req.age(), req.profile(), socialType);
    }

    public static UserResDto fromUser(final User user) {
        return new UserResDto(user.getId(), user.getEmail(), user.getPassword());
    }

    public static JwtDto fromBearerToken(final BearerToken token) {
        return new JwtDto(token.getJwt());
    }

    public static RoleDto fromRoleRequest(final RoleRequest roleRequest) {
        return new RoleDto(null, roleRequest.name(), roleRequest.description());
    }

    public static RoleDto fromRole(final Role role) {
        return new RoleDto(role.getId(), role.getName(), role.getDescription());
    }

    public static ProfileDto fromProfileInfo(final String fileKey, final String originalFileName, final String serverFileName) {
        return new ProfileDto(fileKey, serverFileName, originalFileName);
    }
}
