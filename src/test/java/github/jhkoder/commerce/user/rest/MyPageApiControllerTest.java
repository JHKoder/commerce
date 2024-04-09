package github.jhkoder.commerce.user.rest;

import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.config.WebClientConfig;
import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.cert.repository.CertDslRepository;
import github.jhkoder.commerce.cert.repository.CertRepository;
import github.jhkoder.commerce.cert.service.SignCertService;
import github.jhkoder.commerce.sms.service.SmsFakeService;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import static github.jhkoder.commerce.common.error.ErrorDocument.errorcode;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

public class MyPageApiControllerTest extends RestDocControllerTests {

//
//    @MockBean
//    private SignCertService signCertService;
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private SmsFakeService smsService;
//    @MockBean
//    private EmailService emailService;
//
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private CertDslRepository certDslRepository;
//    @Mock
//    private CertRepository certRepository;
//    @Mock
//    private WebClientConfig webClientConfig;
//
//
//    private final String userApi ="/api/user";
//
//    private final String sellerApi ="/api/seller";
//
//    private final String adminApi ="/api/admin";
//
//
//    @Test
//    @DisplayName("유저 권한 업데이트 요청")
//    void user_role_update() throws Exception {
//
//    }
//
//    @Test
//    @DisplayName("유저 비번 변경")
//    void user_password_change() {
//
//    }
//
//    @Test
//    @DisplayName("유저 가 이메일 & 휴대폰 2차 인증 체크")
//    void user_emailAndPhone_authentication() {
//
//    }
//
//    @Test
//    @DisplayName("유저 가 이메일 변경")
//    void user_email_change() {
//
//    }
//
//    @Test
//    @DisplayName("유저 가 휴대폰 번호 변경")
//    void user_phone_change() {
//
//    }
//
//    @Test
//    @DisplayName("유저가 정보 보기")
//    void user_createDate_see() {
//
//    }
//
//    @Test
//    @DisplayName("판매자 스토어 등록")
//    void seller_store_add() {
//
//    } // 등록 수정 삭제
//
//    @Test
//    @DisplayName("결제 받을 계좌 번호 등록  1원 입금 본인 인증 ")
//    void seller_store_add_receiving_account_number() {
//
//    }
//
//
//    @Test
//    @DisplayName("관리자 가 유저,판매자 현황 보기")
//    void admin_users_status_view() {
//
//    }
//
//
//    @Test
//    @DisplayName("관리자 가 권한 변경 요청이 온 리스트 보기")
//    void admin_member_role_list() {
//
//    }
//
//    @Test
//    @DisplayName("관리자 가 권한 변경 요청 수락")
//    void admin_member_role_accept() {
//    }
//
//    @Test
//    @DisplayName("관리자 가 권한 변경 요청 거절")
//    void admin_member_role_reject() {
//
//    }

}
