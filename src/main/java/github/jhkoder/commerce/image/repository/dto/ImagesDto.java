package github.jhkoder.commerce.image.repository.dto;

public class ImagesDto {
    private Long userId;
    private String path;

    public ImagesDto(Long userId, String path) {
        this.userId = userId;
        this.path = path;
    }
}
