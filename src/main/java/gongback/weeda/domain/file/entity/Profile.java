package gongback.weeda.domain.file.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    private Long id;
    private String fileKey;
    private String serverFileName;
    private String originalFileName;

    @Builder
    public Profile(String fileKey, String serverFileName, String originalFileName) {
        this.fileKey = fileKey;
        this.serverFileName = serverFileName;
        this.originalFileName = originalFileName;
    }
}
