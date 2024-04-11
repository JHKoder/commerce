package github.jhkoder.commerce.user.rest;

import github.jhkoder.commerce.cert.service.MyPageCertService;
import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.auth.WithMockCustomAdmin;
import github.jhkoder.commerce.common.auth.WithMockCustomSeller;
import github.jhkoder.commerce.common.auth.WithMockCustomUser;
import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.store.service.StoreService;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.service.AdminService;
import github.jhkoder.commerce.user.service.UserAuthorizationApplicationService;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.mypage.*;
import github.jhkoder.commerce.user.service.response.MyPageUpCertVerifyResponse;
import github.jhkoder.commerce.user.service.response.MyPageUserResponse;
import github.jhkoder.commerce.user.service.response.SellerStoreResponse;
import github.jhkoder.commerce.user.service.response.ValidEmailAndPhoneResponse;
import github.jhkoder.commerce.user.web.rest.admin.MyPageAdminApiController;
import github.jhkoder.commerce.user.web.rest.seller.MyPageSellerApiController;
import github.jhkoder.commerce.user.web.rest.user.MyPageUserApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static github.jhkoder.commerce.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@WebMvcTest(controllers = {
        MyPageUserApiController.class,
        MyPageSellerApiController.class,
        MyPageAdminApiController.class
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
    @MockBean
    private StoreService storeService;
    @MockBean
    private  AdminService adminService;


    private final String userApi = "/api/user/mypage";

    private final String sellerApi = "/api/seller/mypage";

    private final String adminApi = "/api/admin/mypage";


    @Test
    @WithMockCustomUser
    @DisplayName("유저가 내 정보 조회 ")
    void user_view() throws Exception {
        //given
        String pathAdoc = "mypage/user/view";
        MyPageUserResponse response = new MyPageUserResponse("username", "test@test.com", "01012345678", Role.USER, LocalDateTime.now());
        when(userService.findByMyPageUser(any()))
                .thenReturn(response);

        // when
        ResultActions actions = jsonPostWhen(userApi);

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
                ));

        // adoc custom
        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_NOT_FOUND)
                .disabledRequestFields()
                .build());
    }


    @ParameterizedTest
    @WithMockCustomUser
    @MethodSource("userRoleUpdatesProvider")
    @DisplayName("유저 권한 업데이트 요청")
    void user_role_update(String path) throws Exception {
        //givee
        String pathAdoc = "mypage/role/" + path;
        User user = new User("qwer1234", "username", "1234qwer", "asdf@gmail.com", "01012345678", Gender.MAN, Role.USER);
        when(userService.validEmailAndPhone(any()))
                .thenReturn(user);
        doNothing().when(userService).roleUpdate(any(), any());

        ResultActions actions = jsonPostWhen(userApi + "/role/" + path);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_NOT_FOUND,
                        USER_NOT_EMAIL,
                        USER_NOT_PHONE
                )
                .disabledRequestFields()
                .disabledResponseFields()
                .build());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("유저 비번 변경")
    void user_password_change() throws Exception {
        //givee
        String pathAdoc = "mypage/passwordChange";
        UserPasswordChangeRequest passwordChangeRequest = new UserPasswordChangeRequest("password12");
        String request = objectMapper.writeValueAsString(passwordChangeRequest);
        doNothing().when(userService).passwordChange(any(), any());

        ResultActions actions = jsonPostWhen(userApi + "/password", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_NOT_FOUND
                )
                .addErrorValid(000, "비밀번호: 필수 정보입니다.")
                .addErrorValid(000, "비밀번호: 8~16자의 영문,숫자를 사용해주세요.")
                .disabledRequestFields()
                .disabledResponseFields()
                .build());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("유저 가 이메일 & 휴대폰 2차 인증 체크")
    void user_emailAndPhone_authentication() throws Exception {
        //givee
        String pathAdoc = "mypage/auth/check";
        when(userService.validEmailAndPhoneCheck(any()))
                .thenReturn(new ValidEmailAndPhoneResponse("test@email.com", "010-1234-1234"));

        ResultActions actions = jsonGetWhen(userApi + "/auth/check");

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentResponse(),
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_NOT_FOUND
                )
                .disabledRequestFields()
                .build());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("유저 가 휴대폰 인증 번호 보내기")
    void user_phone_send() throws Exception {
        //givee
        String pathAdoc = "mypage/sms/send";
        MyPageUserSmsSendRequest passwordChangeRequest = new MyPageUserSmsSendRequest("01012345678");
        String request = objectMapper.writeValueAsString(passwordChangeRequest);
        doNothing().when(userService).checkSmsValidAndUnique(passwordChangeRequest.phone());
        doNothing().when(certService).validateSmsVerificationExceed(passwordChangeRequest.phone());
        when(certService.newVerificationCode()).thenReturn(110111);
        doNothing().when(smsService).signupCertSend(passwordChangeRequest.phone(), 110111);
        doNothing().when(certService).saveCert(any());

        ResultActions actions = jsonPostWhen(userApi + "/sms", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_PHONE_UNIQUE,
                        SIGNUP_SMS_EXCEED,
                        SMS_SEND_FAIL
                )
                .disabledResponseFields()
                .build());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("유저 가 휴대폰 변경 완료")
    void user_phone_change() throws Exception {
        //givee
        String pathAdoc = "mypage/sms/check";

        MyPageUserSendValidRequest passwordChangeRequest = new MyPageUserSendValidRequest("01012345678", 100001);
        String request = objectMapper.writeValueAsString(passwordChangeRequest);
        MyPageUpCertVerifyResponse response = new MyPageUpCertVerifyResponse(true);

        when(certService.smsVerifyCodeCheck(any())).thenReturn(response);
        doNothing().when(certService).deleteAuthenticationComplete(any(), any());
        doNothing().when(userService).updatePhone(any(), any());

        ResultActions actions = jsonUpdatesWhen(userApi + "/sms", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("send").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("인증번호")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("인증 성공 여부 T:성공,F:실패")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        SIGNUP_SMS_VERIFY_CODE_FAILED,
                        USER_NOT_FOUND
                )
                .build());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("유저 가 이메일 인증 번호 보내기")
    void user_email_send() throws Exception {
        //given
        String pathAdoc = "mypage/email/send";
        MyPageUserEmailSendRequest passwordChangeRequest = new MyPageUserEmailSendRequest("test@gmail.com");
        String request = objectMapper.writeValueAsString(passwordChangeRequest);

        doNothing().when(userService).checkEmailValidAndUnique(any());
        doNothing().when(certService).validateEmailVerificationExceed(any());
        when(certService.newVerificationCode()).thenReturn(110111);

        doNothing().when(emailService).certSend("test@gmail.com", 110111);

        doNothing().when(certService).saveCert(any());

        ResultActions actions = jsonPostWhen(userApi + "/email", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_EMAIL_UNIQUE,
                        SIGNUP_EMAIL_EXCEED,
                        EMAIL_SEND_PARSE,
                        EMAIL_SEND_AUTHENTICATION,
                        EMAIL_SEND
                )
                .disabledResponseFields()
                .build());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("유저 가 이메일 변경 완료")
    void user_email_change() throws Exception {
        //given
        String pathAdoc = "mypage/email/check";

        MyPageUserSendValidRequest passwordChangeRequest = new MyPageUserSendValidRequest("test@gmail.com", 100001);
        String request = objectMapper.writeValueAsString(passwordChangeRequest);
        MyPageUpCertVerifyResponse response = new MyPageUpCertVerifyResponse(true);

        when(certService.emailVerifyCodeCheck(any())).thenReturn(response);

        doNothing().when(certService).deleteAuthenticationComplete(any(), any());
        doNothing().when(userService).updateEmail(any(), any());

        ResultActions actions = jsonUpdatesWhen(userApi + "/email", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("send").type(JsonFieldType.STRING).description("이멤일"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("인증번호")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("인증 성공 여부 T:성공,F:실패")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        SIGNUP_EMAIL_VERIFY_CODE_FAILED,
                        USER_NOT_FOUND
                )
                .build());
    }


    @Test
    @WithMockCustomSeller
    @DisplayName("판매자 스토어 조회")
    void seller_store_find() throws Exception {
        //given
        String pathAdoc = "mypage/store/get";
        SellerStoreResponse response = new SellerStoreResponse("상호명", "44442141231232", "카카오");

        when(storeService.findByUserId(any())).thenReturn(response);

        ResultActions actions = jsonGetWhen(sellerApi + "/store");

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentResponse(),
                        responseFields(
                                fieldWithPath("tradeName").type(JsonFieldType.STRING).description("상호명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .disabledRequestFields()
                .disabledRequestBody()
                .build());
    }

    @Test
    @WithMockCustomSeller
    @DisplayName("판매자 스토어 추가")
    void seller_store_add() throws Exception {
        //given
        String pathAdoc = "mypage/store/update";
        AddStoreRequest apiRequest = new AddStoreRequest("상호명", "44442141231232", "카카오");
        String request = objectMapper.writeValueAsString(apiRequest);
        SellerStoreResponse response = new SellerStoreResponse("상호명", "44442141231232", "카카오");

        when(storeService.addStore(any(), any())).thenReturn(response);

        ResultActions actions = jsonPostWhen(sellerApi + "/store", request);

        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("tradeName").type(JsonFieldType.STRING).description("상호명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름")
                        ),
                        responseFields(
                                fieldWithPath("tradeName").type(JsonFieldType.STRING).description("상호명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .build());
    }

    @Test
    @WithMockCustomSeller
    @DisplayName("판매자 스토어 수정")
    void seller_store_update() throws Exception {
        //given
        String pathAdoc = "mypage/store/update";
        UpdateStoreRequest apiRequest = new UpdateStoreRequest("상호명", "44442141231232", "카카오");
        String request = objectMapper.writeValueAsString(apiRequest);
        SellerStoreResponse response = new SellerStoreResponse("상호명", "44442141231232", "카카오");

        when(storeService.update(any(), any())).thenReturn(response);

        ResultActions actions = jsonUpdatesWhen(sellerApi + "/store", request);

        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("tradeName").type(JsonFieldType.STRING).description("상호명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름")
                        ),
                        responseFields(
                                fieldWithPath("tradeName").type(JsonFieldType.STRING).description("상호명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .build());
    }

    @Test
    @WithMockCustomSeller
    @DisplayName("판매자 스토어 삭제")
    void seller_store_delete() throws Exception {
        //given
        String pathAdoc = "mypage/store/delete";
        doNothing().when(storeService).delete(any());

        ResultActions actions = jsonDeleteWhen(sellerApi + "/store");

        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .disabledRequestBody()
                .disabledRequestFields()
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
    }

    @Test
    @WithMockCustomAdmin
    @DisplayName("관리자 가 유저,판매자 현황 보기")
    void admin_users_status_view() throws Exception {
        // USER, ADMIN, SELLER 상황을 10 단위로 페이징 해서 보여준다.
        //given
        String pathAdoc = "mypage/admin/users";
        AdminDashBoardRequest adminDashBoardRequest = new AdminDashBoardRequest(1);
        String request = objectMapper.writeValueAsString(adminDashBoardRequest);
        List<AdminUsersDashBoardResponse.Users> userList = new ArrayList<>();
        List<AdminUsersDashBoardResponse.Users> sellerList = new ArrayList<>();
        List<AdminUsersDashBoardResponse.Users> adminList = new ArrayList<>();

        userList.add(new AdminUsersDashBoardResponse.Users("userName","user@gmail.com","01012341234",Gender.MAN,Role.USER));
        sellerList.add(new AdminUsersDashBoardResponse.Users("sellerName","seller@gmail.com","01012341231",Gender.MAN,Role.SELLER));
        adminList.add(new AdminUsersDashBoardResponse.Users("adminName","admin@gmail.com","01012121212",Gender.WOMAN,Role.ADMIN));

        AdminUsersDashBoardResponse response = new AdminUsersDashBoardResponse(userList,sellerList,adminList,
                1,1);

        when(adminService.allUserList(anyInt())).thenReturn(response);


        ResultActions actions = jsonGetWhen(adminApi + "/users",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("users[].username").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("users[].email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("users[].phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("users[].gender").type("Enum[MAN,WOMAN]").description("성별"),
                                fieldWithPath("users[].role").type("Enum[USER,SELLER,ADMIN]").description("권한"),

                                fieldWithPath("sellers[].username").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("sellers[].email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("sellers[].phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("sellers[].gender").type("Enum[MAN,WOMAN]").description("성별"),
                                fieldWithPath("sellers[].role").type("Enum[USER,SELLER,ADMIN]").description("권한"),

                                fieldWithPath("admins[].username").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("admins[].email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("admins[].phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("admins[].gender").type("Enum[MAN,WOMAN]").description("성별"),
                                fieldWithPath("admins[].role").type("Enum[USER,SELLER,ADMIN]").description("권한"),


                                fieldWithPath("total").type(JsonFieldType.NUMBER).description("total"),
                                fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("page 번호")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .build());
    }


    @Test
    @WithMockCustomAdmin
    @DisplayName("관리자 가 권한 변경 요청이 온 리스트 보기")
    void admin_member_role_list() throws Exception {
        //given
        String pathAdoc = "mypage/admin/role/list";
        List<AdminRequestRoleListResponse> list = new ArrayList<>();
        list.add(new AdminRequestRoleListResponse(1L,1L,Role.ADMIN, OracleBoolean.F));
        list.add(new AdminRequestRoleListResponse(2L,3L,Role.ADMIN, OracleBoolean.F));
        when(adminService.memberRoleRequestList()).thenReturn(list);


        ResultActions actions = jsonGetWhen(adminApi + "/roles");

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        responseFields(
                                fieldWithPath("[].uaaId").type(JsonFieldType.NUMBER).description("권한 신청.id"),
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("권한 신청한 유저 id"),
                                fieldWithPath("[].role").type("Enum[ADMIN,SELLER,USER]").description("신청한 권한"),
                                fieldWithPath("[].isApprove").type("Enum[T,F]").description("T: 승인 , F: 거부 ")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .disabledRequestFields()
                .disabledRequestBody()
                .build());
    }

    @Test
    @WithMockCustomAdmin
    @DisplayName("관리자 가 권한 변경 요청 수락 & 거절")
    void admin_member_role_reject() throws Exception {
        //given
        String pathAdoc = "mypage/admin/role/accept";
        AdminMemberRoleRequest roleRequest = new AdminMemberRoleRequest(1L,true);
        String request = objectMapper.writeValueAsString(roleRequest);
        doNothing().when(adminService).memberRoleUpdate(anyLong(), anyBoolean());

        ResultActions actions = jsonUpdatesWhen(adminApi + "/role", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("targetUserId").type(JsonFieldType.NUMBER).description("타겟 User.id"),
                                fieldWithPath("ok").type(JsonFieldType.BOOLEAN).description("true: 수락 , false : 거절")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        USER_NOT_FOUND,
                        ADMIN_TARGET_USER_NO_PERMISSION_REQUEST
                )
                .disabledResponseFields()
                .disabledResponseBody()
                .build());
    }


    private static Stream<Arguments> userRoleUpdatesProvider() {
        return Stream.of(
                Arguments.of("seller"),
                Arguments.of("admin")
        );
    }


}