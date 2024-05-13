package github.jhkoder.commerce.store.rest;

import github.jhkoder.commerce.config.WebClientConfigTest;
import github.jhkoder.commerce.pay.service.BootPayManager;
import github.jhkoder.commerce.pay.service.PayService;
import github.jhkoder.commerce.user.repository.UserRepository;
import kr.co.bootpay.Bootpay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@DisplayName("결제 api test")
public class PayServiceTest {
    private WebClient client = new WebClientConfigTest().webClient();

    @Autowired
    private PayService payService;
    @Autowired
    private UserRepository userRepository;


    @Mock
    private Bootpay bootpayMock;

    private BootPayManager bootPayManager;

    @Test
    @DisplayName("결제 신청")
    void pay(){
        payService.pay();
//        payService.bootPayUserToken("user");
    }


    // 단건 조회
    // 결제 서버 승인
    // 결제 취소
    // 구매자 토큰 발급 받기
    // 서버로 통지 받기


}
