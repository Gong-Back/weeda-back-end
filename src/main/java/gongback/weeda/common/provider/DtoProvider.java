package gongback.weeda.common.provider;

import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.jwt.BearerToken;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.JwtDto;
import gongback.weeda.service.dto.SignUpDto;
import gongback.weeda.service.dto.UserResDto;

public class DtoProvider {
    public static SignUpDto fromRequest(SignUpRequest req, SocialType socialType) {
        return new SignUpDto(req.email(), req.password(), req.name(), req.nickname(), req.gender(),
                req.age(), req.profile(), socialType);
    }

    public static UserResDto fromUser(User user) {
        return new UserResDto(user.getId(), user.getEmail(), user.getPassword());
    }

    public static JwtDto fromBearerToken(BearerToken token) {
        return new JwtDto(token.getJwt());
    }
}
