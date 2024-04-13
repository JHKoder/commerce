package github.jhkoder.commerce.image;

import github.jhkoder.commerce.image.repository.ImageJschRepository;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import github.jhkoder.commerce.image.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class ImageServiceTest {
    @Autowired
    private ImageJschRepository jschRepository;

    @Autowired
    private ImageService imageService;


    @Test
    void 삭제_시간() throws IOException {
        ImageRequest request = file("src/main/resources/static/img/testimg.png",
                "testimg.png", "image/png");

        String serverPath = imageService.upload(request);

        long startTime = System.currentTimeMillis();
        imageService.delete(serverPath);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("삭제 time: " + elapsedTime + " milliseconds");
    }


    @Test
    void 쓰기_시간() throws IOException {
        // 테스트용 이미지 파일을 바이트 배열로 읽어옵니다.
        ImageRequest request = file("src/main/resources/static/img/testimg.png",
                "testimg.png", "image/png");

        long startTime = System.currentTimeMillis();
        String serverPath = imageService.upload(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("쓰기 time: " + elapsedTime + " milliseconds");
        imageService.delete(serverPath);
    }

    @Test
    void 읽기_시간() {
        long startTime = System.currentTimeMillis();
        imageService.read("aa/4893cd-76e2-4086-84b5-7a77a0abe4b6_testimg.png");
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("쓰기 time: " + elapsedTime + " milliseconds");
    }

    private ImageRequest file(String imagePath, String imageName, String type) throws IOException {
        Path path = Paths.get(imagePath);
        byte[] content = Files.readAllBytes(path);
        MultipartFile multipartFile = new MockMultipartFile("file", imageName, type, content);
        return new ImageRequest(multipartFile);
    }
}
