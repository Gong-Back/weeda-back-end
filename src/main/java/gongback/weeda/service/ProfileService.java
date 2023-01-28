package gongback.weeda.service;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.provider.DtoProvider;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.domain.file.entity.Profile;
import gongback.weeda.domain.file.repository.ProfileRepository;
import gongback.weeda.service.type.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final FileService fileService;
    private final ProfileRepository profileRepository;

    public Mono<Long> uploadProfile(FilePart file, FileType fileType) {
        String originalFileName = file.filename();
        String serverFileName = fileService.createFileKey(fileService.getExt(file.filename()));

        return fileService.uploadFile(file, fileType, serverFileName)
                .map(fileKey -> DtoProvider.fromProfileInfo(fileKey, originalFileName, serverFileName))
                .flatMap(fileDto -> profileRepository.save(EntityProvider.fromProfileDto(fileDto)))
                .map(Profile::getId);
    }

    public Mono<String> getFileURL(String fileKey) {
        return profileRepository.existsByFileKey(fileKey)
                .map(it -> isValidFileKey(it, fileKey))
                .flatMap(fileService::getFileURL);
    }

    private String isValidFileKey(boolean isExist, String fileKey) {
        if (!isExist) {
            throw new WeedaApplicationException(ResponseCode.NOT_FOUND, "FileKey is not existed. fileKey = " + fileKey);
        }
        return fileKey;
    }
}
