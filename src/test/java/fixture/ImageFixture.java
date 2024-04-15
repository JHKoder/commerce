package fixture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.store.service.request.StoreAddProductRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static fixture.UserFixture.getFixtureUser;

public class ImageFixture {

    @JsonIgnore
    public static MultipartFile getFixtureFile() {
        String fileName = "test.txt";
        byte[] fileContent = "Hello, World!".getBytes();
        return new MockMultipartFile(fileName, fileName, "text/plain", fileContent);
    }


    public static Images getFixtureImages() {
        return new Images(getFixtureUser(), "");
    }

    public static StoreAddProductRequest.CustomMultiPartFile getFixtureMockMultipartFile() {
        byte[] imageData = "<<png data>>".getBytes();
        MultipartFile image = new MockMultipartFile("image", "image.png", "image/png", imageData);
        StoreAddProductRequest.CustomMultiPartFile file = new StoreAddProductRequest.CustomMultiPartFile();
//        file.add(image);
        return file;
    }

}
