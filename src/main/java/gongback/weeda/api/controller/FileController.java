package gongback.weeda.api.controller;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.provider.EntityProvider;
import gongback.weeda.service.FileService;
import gongback.weeda.service.ProfileService;
import gongback.weeda.service.type.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final ProfileService profileService;

    @GetMapping("/url")
    public Mono<ResponseEntity> getFileUrl(@RequestParam("fileKey") String fileKey,
                                           @RequestParam("fileType") FileType fileType) {
        if (fileType == FileType.PROFILE) {
            return profileService.getFileURL(fileKey)
                    .map(it -> EntityProvider.response(ResponseCode.OK, it));
        }
        return null;
    }
}
