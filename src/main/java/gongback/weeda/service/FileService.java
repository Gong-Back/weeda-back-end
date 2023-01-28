package gongback.weeda.service;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.type.ProfileType;
import gongback.weeda.service.type.FileType;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.prodFileSavePath}")
    private String prodFileSavePath;
    private final Environment environment;
    private final AmazonS3Service amazonS3Service;

    public Mono<String> uploadFile(FilePart file, FileType fileType, String serverFileName) {
        String dirPath = getFilePath(fileType);
        makeDir(dirPath);
        Path filePath = Paths.get(dirPath, serverFileName);

        return writeFile(file, filePath)
                .flatMap(it -> amazonS3Service.uploadFileAndGetFileKey(file, fileType, serverFileName, filePath));
    }
    public Mono<String> getFileURL(String fileKey) {
        return amazonS3Service.getFileURL(fileKey);
    }

    private String getFilePath(FileType type) {
        String[] currentProfile = environment.getActiveProfiles();
        String dirPath = Paths.get("").toAbsolutePath().toString();

        if (currentProfile.length != 0 &&
                currentProfile[0].equals(ProfileType.PROD.getProfileName())) {
            dirPath = prodFileSavePath;
        }

        return dirPath + amazonS3Service.getSaveFilePath(type);
    }

    private void makeDir(String dirPath) {
        File dir = new File(dirPath);
        boolean isSuccess = false;

        if (!dir.exists()) {
            isSuccess = dir.mkdirs();
        }

        if (!dir.exists() && !isSuccess) {
            throw new WeedaApplicationException(ResponseCode.INTERNAL_SERVER_ERROR, "Folder Created Error.");
        }
    }

    protected String getExt(String fileName) {
        if (fileName == null) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    protected String createFileKey(String ext) {
        return UUID.randomUUID() + "." + ext;
    }

    private Mono<String> writeFile(FilePart file, Path basePath) {
        return DataBufferUtils.write(file.content(), basePath)
                .thenReturn(basePath.toString());
    }
}
