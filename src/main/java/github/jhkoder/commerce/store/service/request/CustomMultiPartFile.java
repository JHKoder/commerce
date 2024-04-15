package github.jhkoder.commerce.store.service.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CustomMultiPartFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long imageId;
    private MultipartFile image;
}
