package gongback.weeda.utils;

import gongback.weeda.api.controller.request.SignUpRequest;
import gongback.weeda.common.type.SocialType;
import gongback.weeda.domain.user.entity.User;
import gongback.weeda.service.dto.SignUpDto;
import gongback.weeda.service.dto.UserResDto;

public class CreateDtoUtil {
    public static SignUpDto fromRequest(SignUpRequest req, SocialType socialType) {
        return new SignUpDto(req.email(), req.password(), req.name(), req.nickname(), req.gender(),
                req.age(), req.profile(), socialType);
    }

    public static UserResDto fromUser(User user) {
        return new UserResDto(user.getId(), user.getEmail());
    }
}
