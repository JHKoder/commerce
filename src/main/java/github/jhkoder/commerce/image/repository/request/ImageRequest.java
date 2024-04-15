package github.jhkoder.commerce.image.repository.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ImageRequest implements Serializable {

    public MultipartFile file;

    public String getFileName() {
        return file.getOriginalFilename();
    }

    public ImageRequest(MultipartFile file) {
        this.file = file;
    }
}
