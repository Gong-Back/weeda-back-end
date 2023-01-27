package gongback.weeda.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("s3")
public class S3Properties {
    private final String awsAccessKey;
    private final String awsSecretKey;

    private final String urlValidTime;
    private final String bucketName;
    private final String fileFolder;
    private final String profileFolder;
    private final String defaultProfile;
}
