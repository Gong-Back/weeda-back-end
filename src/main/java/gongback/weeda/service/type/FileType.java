package gongback.weeda.service.type;

public enum FileType {
    PROFILE("프로필 이미지"), FILE("일반 이미지");

    private final String description;

    FileType(String description) {
        this.description = description;
    }
}
