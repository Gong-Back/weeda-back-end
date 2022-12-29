package gongback.weeda.common.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SocialType {

    WEEDA("앱"), KAKAO("카카오"), GOOGLE("구글"), APPLE("애플");


    private final String korName;
}
