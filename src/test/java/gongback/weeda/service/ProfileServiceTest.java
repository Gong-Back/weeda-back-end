package gongback.weeda.service;

import gongback.weeda.common.TestProvider;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.domain.file.entity.Profile;
import gongback.weeda.domain.file.repository.ProfileRepository;
import gongback.weeda.service.type.FileType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    ProfileService profileService;

    @Mock
    FileService fileService;

    @Mock
    ProfileRepository profileRepository;

    @Test
    @DisplayName("프로필 이미지 업로드 성공(ProfileService)")
    void givenFileAndType_thenSuccess() throws Exception {
        // given
        FileType fileType = FileType.PROFILE;
        FilePart profile = Mockito.mock(FilePart.class);
        Profile testProfile = TestProvider.createTestProfile();
        String fileKey = testProfile.getFileKey();
        String profileName = testProfile.getOriginalFileName();
        String serverFileName = testProfile.getServerFileName();
        Long profileId = new Random().nextLong(0, 100);
        ReflectionTestUtils.setField(testProfile, "id", profileId);

        // when
        when(profile.filename()).thenReturn(profileName);
        when(fileService.createFileKey(any())).thenReturn(serverFileName);
        when(fileService.uploadFile(profile, fileType, serverFileName))
                .thenReturn(Mono.just(fileKey));
        when(profileRepository.save(any(Profile.class))).thenReturn(Mono.just(testProfile));

        // then
        StepVerifier.create(profileService.uploadProfile(profile, fileType))
                .expectNextMatches(id -> id.equals(profileId))
                .verifyComplete();
    }

    @Test
    @DisplayName("프로필 이미지 URL 조회 성공(ProfileService)")
    void givenFileKey_thenSuccess() throws Exception {
        // given
        String fileKey = "profile/server-save-test-image.jpeg";
        String fileUrl = "https://test-temp-url.com/profile/server-save-test-image.jpeg";

        // when
        when(profileRepository.existsByFileKey(fileKey))
                .thenReturn(Mono.just(true));
        when(fileService.getFileURL(fileKey))
                .thenReturn(Mono.just(fileUrl));

        // then
        StepVerifier.create(profileService.getFileURL(fileKey))
                .expectNextMatches(it -> it.equals(fileUrl))
                .verifyComplete();
    }

    @Test
    @DisplayName("프로필 이미지 URL 조회 실패(ProfileService)")
    void givenFileKey_whenNotExists_thenFail() throws Exception {
        // given
        String fileKey = "profile/wrong.jpeg";

        // when
        when(profileRepository.existsByFileKey(fileKey))
                .thenReturn(Mono.just(false));

        // then
        StepVerifier.create(profileService.getFileURL(fileKey))
                .expectError(WeedaApplicationException.class)
                .verify();

    }
}