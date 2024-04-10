package github.jhkoder.commerce.user.rest;

import github.jhkoder.commerce.cert.service.MyPageCertService;
import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.auth.WithMockCustomUser;
import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.service.UserAuthorizationApplicationService;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.response.MyPageUserResponse;
import github.jhkoder.commerce.user.web.rest.user.MyPageUserApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static github.jhkoder.commerce.exception.ErrorCode.USER_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@WebMvcTest(controllers = {
        MyPageUserApiController.class
})
@DisplayName("마이페이지")
public class MyPageApiControllerTest extends RestDocControllerTests {

    @MockBean
    private UserAuthorizationApplicationService uaaService;
    @MockBean
    private MyPageCertService certService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private UserService userService;
    @MockBean
    private SmsService smsService;


    private final String userApi = "/api/user/mypage";

    private final String sellerApi = "/api/seller/mypage";

    private final String adminApi = "/api/admin/mypage";


    @Test
    @WithMockCustomUser
    @DisplayName("내 정보 조회 ")
    void user_view() throws Exception {
        //given

        String pathAdoc = "mypage/user/view";
        MyPageUserResponse response = new MyPageUserResponse("username", "test@test.com", "01012345678", Role.USER, LocalDateTime.now());
        when(userService.findByMyPageUser(any()))
                .thenReturn(response);

        ResultActions actions = jsonPostWhen("/api/user/mypage/");

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentResponse(),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("role").type("Enum[USER,SELLER,ADMIN]").description("권한"),
                                fieldWithPath("createTime").type("localDateTime").description("회원 가입 일자")
                        )
                ))
                .andDo(document(pathAdoc,
                                preprocessResponse(prettyPrint())
                        )
                );

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_NOT_FOUND,
                        USER_NOT_FOUND,
                        USER_NOT_FOUND)
                .disabledRequestFields()
                .build());
    }


    @Test
    @DisplayName("유저 권한 업데이트 요청")
    void user_role_update() throws Exception {

    }

    @Test
    @DisplayName("유저 비번 변경")
    void user_password_change() {

    }

    @Test
    @DisplayName("유저 가 이메일 & 휴대폰 2차 인증 체크")
    void user_emailAndPhone_authentication() {

    }

    @Test
    @DisplayName("유저 가 이메일 변경")
    void user_email_change() {

    }

    @Test
    @DisplayName("유저 가 휴대폰 번호 변경")
    void user_phone_change() {

    }

    @Test
    @DisplayName("유저가 정보 보기")
    void user_createDate_see() {

    }

    @Test
    @DisplayName("판매자 스토어 등록")
    void seller_store_add() {

    } // 등록 수정 삭제

    @Test
    @DisplayName("결제 받을 계좌 번호 등록  1원 입금 본인 인증 ")
    void seller_store_add_receiving_account_number() {

    }


    @Test
    @DisplayName("관리자 가 유저,판매자 현황 보기")
    void admin_users_status_view() {

    }


    @Test
    @DisplayName("관리자 가 권한 변경 요청이 온 리스트 보기")
    void admin_member_role_list() {

    }

    @Test
    @DisplayName("관리자 가 권한 변경 요청 수락")
    void admin_member_role_accept() {
    }

    @Test
    @DisplayName("관리자 가 권한 변경 요청 거절")
    void admin_member_role_reject() {

    }

}