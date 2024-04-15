package github.jhkoder.commerce.image.service.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUpdateRequest{

    private Long imageId;
    private boolean isUpdate;
    private MultipartFile image;

    public ImageUpdateRequest(Long id, boolean isUpdate){
        this.imageId=id;
        this.isUpdate=isUpdate;
    }
}
