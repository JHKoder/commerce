package github.jhkoder.commerce.flatform.local.itemcoupon.service;

import fixture.CategoryFixture;
import fixture.UserFixture;
import github.jhkoder.commerce.config.WebClientConfigTest;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.ItemCoupon;
import github.jhkoder.commerce.flatform.local.ep.itemevent.domain.ItemEvent;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import github.jhkoder.commerce.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import static fixture.ItemFixture.getItemFixture;
import static fixture.ItemProductFixture.getItemProductFixture;

//@RunWith(SpringRunner.class)
//@DataJpaTest
@DisplayName("쿠폰")
public class CouponServiceTest {


    User user;
    Category category;
    Item item;
    ItemProduct itemProduct;
    ItemEvent event;

    private ImageRequest file(String imagePath, String imageName, String type) throws IOException {
        Path path = Paths.get(imagePath);
        byte[] content = Files.readAllBytes(path);
        MultipartFile multipartFile = new MockMultipartFile("file", imageName, type, content);
        return new ImageRequest(multipartFile);
    }

//    @BeforeEach
//    void setup() throws IOException {
//        user = UserFixture.getFixtureUser();
//        category = CategoryFixture.getCategoryFixture();
//        item = getItemFixture();
//        itemProduct = getItemProductFixture(user, category, item);
//
//        int amount = 10_000; // 가격 인하
//        String work = " 할인 문구 "; // 차후 이미지도 추가 할 수 있는 여지를 두자
//
//        ImageRequest request = file("src/main/resources/static/img/testimg.png",
//                "testimg.png", "image/png");
//
//
//        event = new ItemEvent(itemProduct, category, amount, work, false, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
//    }

    @Test
    void 등록() {
        // 판매자가 쿠폰을 생성합니다. 이때 쿠폰의 유형, 할인 금액 또는 비율, 사용 가능 기간 등을 지정합니다.
//        String code = UUID.randomUUID().toString();
//        int downPrice = 10_000;
//        ItemCoupon coupon = new ItemCoupon(itemProduct, category,1, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
//

    }


    @Test
    void QR생성() {
        // qr 코드 하루에 최대 99,999 권 까지 생성 가능
//        int size = 150;
//        int size_max = 1000;
//        int size_min = 10;
//        String pattern = "PNG|GIF|JPEG|JPG|SVG|EPS"; //  전부 소문자 형태 & default : png
//
//        WebClientConfigTest client = new WebClientConfigTest();
//        WebClient webClient = client.webClient();
//        String host = "https://api.qrserver.com/v1/create-qr-code";
//        String code = UUID.randomUUID().toString();
//        String data = "http://localhost:8080/coupon/qr/products/" + itemProduct.getId() + "/" + code;

    }

    @Test
    void 발급() {
        // 사용자가 쿠폰을 발급받습니다. 이는 일반적으로 쿠폰 코드를 받는 방식이며, 이 코드를 이용하여 쿠폰을 활성화합니다.
        /*
            token 방식과 , qr 방식 이 존재함
         */
    }

    @Test
    void 활성화() {
        //사용자가 쿠폰을 활성화하여 적용 가능한 상태로 만듭니다. 이때 쿠폰의 유효성을 검사하고, 사용 가능한지 여부를 판단합니다.
        /* qr& 쿠폰값 적용시  발생함
        1.활성화 요청 qr 코드에 관련된 활성화
        2. 쿠폰 유효성 검사
        3. 쿠폰 활성화 처리
            유효하다면 쿠폰을 활성화 하여 사용 가능한 상태로 변경함
            이떄 쿠폰의 활성화 여부를 사용자 계정 또는 세션정보와 연결하여 저장함
            사용자 인증 로그인한사용자인지
        4. 쿠폰 활성화 결과 반환 적용된 쿠폰으로 결제 진행
         */
    }

    @Test
    void 적용() {
        //사용자가 구매할 때 쿠폰을 적용합니다. 이때 쿠폰의 할인 금액 또는 비율에 따라 결제 금액이 조정됩니다.
    }

    @Test
    void 사용() {
        //적용된 쿠폰이 결제에 반영되고, 해당 쿠폰은 사용된 것으로 표시됩니다. 이때 쿠폰의 재사용이 불가능하도록 처리됩니다.
    }


    @Test
    void 검증() {
        //쿠폰이 유효한지 확인합니다. 쿠폰의 유효 기간, 사용 가능한 제품 또는 카테고리, 사용자의 보유 조건 등을 검사합니다.
    }

    @Test
    void 할인금액계산() {
        //쿠폰의 유형에 따라 할인 금액을 계산합니다. 일정 금액을 할인하거나, 일정 비율로 할인하는 등의 방식이 있습니다
    }

    @Test
    void 적용제한() {
        //쿠폰의 적용 가능 여부를 결정합니다. 예를 들어, 한 번에 하나의 쿠폰만 적용할 수 있도록 제한하거나, 특정 상품에만 쿠폰을 적용할 수 있도록 제한할 수 있습니다.
    }

    @Test
    void 사용이력관리() {
        //사용자의 쿠폰 사용 이력을 관리합니다. 어떤 쿠폰이 언제 사용되었는지를 기록하여 중복 사용을 방지하고, 추적할 수 있도록 합니다.
    }

    @Test
    void 쿠폰반영() {
        //쿠폰이 결제에 반영되어 최종 결제 금액이 조정됩니다. 이때 쿠폰의 할인 금액이나 비율에 따라 결제 금액이 조정됩니다.
    }
}
