package github.jhkoder.commerce.image.repository.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ImagePathRequest {

    private final List<String> paths;
    private final String fileName;
    private final String fullName;

    public ImagePathRequest(List<String> paths, String fileName,String fullName) {
        this.paths = paths;
        this.fileName = fileName;
        this.fullName=fullName;
    }
}
