package gongback.weeda.domain.user.entity;

import gongback.weeda.domain.base.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String gender;
    private Integer age;
    private String profileKey;
    private String socialType;

    @Builder
    public User(String email, String password, String name, String nickname, String gender, Integer age, String profileKey, String socialType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.profileKey = profileKey;
        this.socialType = socialType;
    }
}
