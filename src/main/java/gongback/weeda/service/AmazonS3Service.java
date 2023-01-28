package gongback.weeda.service;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.properties.S3Properties;
import gongback.weeda.service.type.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private final S3Presigner preSigner;
    private final S3Properties s3Properties;
    private final S3AsyncClient s3AsyncClient;

    public Mono<String> uploadFileAndGetFileKey(FilePart file, FileType type, String fileName, Path filePath) {
        String bucketName = s3Properties.getBucketName();
        String fileKey = getSaveFilePath(type) + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        CompletableFuture<PutObjectResponse> future =
                s3AsyncClient.putObject(objectRequest,
                AsyncRequestBody.fromFile(filePath)
        );

        future.whenComplete((resp, err) -> {
            try {
                if (resp == null) {
                    err.printStackTrace();
                    throw new WeedaApplicationException(ResponseCode.INTERNAL_SERVER_ERROR, "File Upload Failed.");
                }
            } finally {
                file.delete();
                s3AsyncClient.close();
            }
        });

        return Mono.just(fileKey);
    }

    public String getSaveFilePath(FileType type) {
        return type == FileType.PROFILE ? s3Properties.getProfileFolder() : s3Properties.getFileFolder();
    }

    public Mono<String> getFileURL(String fileKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(fileKey)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(Long.parseLong(s3Properties.getUrlValidTime())))
                .getObjectRequest(getObjectRequest)
                .build();

        return Mono.fromFuture(CompletableFuture.supplyAsync(() ->
                preSigner
                        .presignGetObject(getObjectPresignRequest)
                        .url()
                        .toString()
        ));
    }

    public String getDefaultProfileKey() {
        return s3Properties.getProfileFolder() + s3Properties.getDefaultProfile();
    }
}
