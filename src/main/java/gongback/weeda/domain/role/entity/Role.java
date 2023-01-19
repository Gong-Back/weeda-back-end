package gongback.weeda.domain.role.entity;

import gongback.weeda.domain.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {

    @Id
    private Long id;
    private String name;
    private String description;

    @Builder
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
