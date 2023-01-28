package gongback.weeda.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileType {
    LOCAL("local")
    , PROD("prod")
    ;

    private final String profileName;
}
