package github.jhkoder.commerce.image.repository.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

@Data
public class ImageRequest implements Serializable {
    private MultipartFile multipartFile;

    public String getFileName() {
        return multipartFile.getOriginalFilename();
    }
    public InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }
}
