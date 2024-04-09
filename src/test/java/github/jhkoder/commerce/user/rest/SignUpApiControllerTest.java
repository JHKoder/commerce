package github.jhkoder.commerce.user.rest;

import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.config.WebClientConfig;
import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.cert.repository.CertDslRepository;
import github.jhkoder.commerce.cert.repository.CertRepository;
import github.jhkoder.commerce.cert.service.SignCertService;
import github.jhkoder.commerce.cert.service.request.SignUpCertVerifyRequest;
import github.jhkoder.commerce.sms.service.SmsFakeService;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.signup.SignUpRequest;
import github.jhkoder.commerce.user.service.response.SignUpCertVerifyResponse;
import github.jhkoder.commerce.user.service.response.SignUpIdCheckResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static github.jhkoder.commerce.common.error.ErrorDocument.errorcode;
import static github.jhkoder.commerce.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@DisplayName("회원 가입 API ")
public class SignUpApiControllerTest extends RestDocControllerTests {

    @MockBean
    private SignCertService signCertService;
    @MockBean
    private UserService userService;
    @MockBean
    private SmsFakeService smsService;
    @MockBean
    private EmailService emailService;


    @Mock
    private UserRepository userRepository;
    @Mock
    private CertDslRepository certDslRepository;
    @Mock
    private CertRepository certRepository;
    @Mock
    private WebClientConfig webClientConfig;

    private final String defaultUri = "/signup/api";


    @Test
    @DisplayName("회원 가입 실행")
    void signup() throws Exception {
        // given
        String pathAdoc = "signup";
        SignUpRequest signUpRequest = new SignUpRequest("userId", "userName", "password", "mail@email.com", "01012345678", Gender.MAN, CertAuthentication.EMAIL);
        String request = objectMapper.writeValueAsString(signUpRequest);
        doNothing().when(signCertService)
                .validateCert(any());
        doNothing().when(userService)
                .validateMemberRegistration(signUpRequest);
        doNothing().when(userService)
                .signup(any());

        // when
        ResultActions actions = jsonWhen( "",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 ").attributes(optional()),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰").attributes(optional()),
                                fieldWithPath("gender").type("Enum").description("성별 타입 : [MAN],[WOMAN] "),
                                fieldWithPath("authenticationType").type("Enum").description("인증 타입 : [PHONE],[EMAIL] ")
                        )
                ));

        autoDoc(pathAdoc,
                errorcode(SIGNUP_SMS_EXCEED),
                errorcode(SIGNUP_SMS_VERIFY_CODE_FAILED),
                errorcode(SIGNUP_SMS_DUPLICATE),
                errorcode(SIGNUP_EMAIL_EXCEED),
                errorcode(SIGNUP_EMAIL_VERIFY_CODE_FAILED),
                errorcode(SIGNUP_EMAIL_DUPLICATE),
                errorcode(SIGNUP_CERT_CODE_UNVERIFIED)
        );
    }

    @Test
    @DisplayName("아이디 체크")
    void idCheck() throws Exception {
        // given
        String pathAdoc = "signup/idCheck";
        String request = strToJson("id", "testId");
        SignUpIdCheckResponse response = new SignUpIdCheckResponse(true);
        when(userService.isIdCheck(any())).thenReturn(response);

        // when
        ResultActions actions = jsonWhen( "/idCheck",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("아이디").attributes(optional())
                        ),
                        responseFields(
                                fieldWithPath("isCheck").type(JsonFieldType.BOOLEAN).description("[true]: 아이디 사용 가능 ,[false]: 아이디 중복 으로 사용 불가\n false 대신 error code 403 이 발생함 ")
                        )
                ));

        autoDoc(pathAdoc);
    }

    @Test
    @DisplayName("SMS 인증 번호 보내기")
    void smsSend() throws Exception {
        //given
        String pathAdoc = "signup/smsCertSend";
        String request = strToJson("sms", "01022529468");
        doNothing().when(userService).signup(any());
        doNothing().when(signCertService).validateSmsVerificationExceed(any());
        when(signCertService.newVerificationCode()).thenReturn(1);
        doNothing().when(smsService).signupCertSend("821012345678", 1);
        doNothing().when(signCertService).saveCert(any());

        // when
        ResultActions actions = jsonWhen( "/cert/sms/send",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("sms").type(JsonFieldType.STRING).description("휴대폰 번호")
                        )
                ));

        autoDoc(pathAdoc,
                errorcode(USER_PHONE_UNIQUE),
                errorcode(SIGNUP_SMS_EXCEED),
                errorcode(SMS_SEND_FAIL)
        );
    }


    @Test
    @DisplayName("email 인증 번호 보내기")
    void emailSend() throws Exception {
        //given
        String pathAdoc = "signup/emailCertSend";
        String request = strToJson("email", "test@naver.com");
        doNothing().when(userService).checkEmailValidAndUnique(any());
        doNothing().when(signCertService).validateEmailVerificationExceed(any());
        when(signCertService.newVerificationCode()).thenReturn(1);
        doNothing().when(emailService).signupCertSend("test@naver.com", 1);
        doNothing().when(signCertService).saveCert(any());

        // when
        ResultActions actions = jsonWhen("/cert/email/send", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        )
                ));

        autoDoc(pathAdoc,
                errorcode(USER_EMAIL_UNIQUE),
                errorcode(SIGNUP_EMAIL_EXCEED),
                errorcode(EMAIL_SEND_PARSE),
                errorcode(EMAIL_SEND_AUTHENTICATION),
                errorcode(EMAIL_SEND)
        );
    }

    @Test
    @DisplayName("SMS 인증 번호 검증 ")
    void smsSendVerifyCheck() throws Exception {
        //given
        String pathAdoc = "signup/smsSendVerifyCheck";
        SignUpCertVerifyRequest certVerifyRequest = new SignUpCertVerifyRequest("01012345678", 123456);
        String request = objectMapper.writeValueAsString(certVerifyRequest);
        SignUpCertVerifyResponse response = new SignUpCertVerifyResponse(true);
        when(signCertService.smsVerifyCodeCheck(certVerifyRequest)).thenReturn(response);

        // when
        ResultActions actions = jsonWhen( "/cert/sms/verify",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("send").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("true : 성공 , false: 실패")
                        )
                ));

        autoDoc(pathAdoc,
                errorcode(SIGNUP_SMS_VERIFY_CODE_FAILED));
    }


    @Test
    @DisplayName("Email 인증 번호 검증 ")
    void emailSendVerifyCheck() throws Exception {
        //given
        String pathAdoc = "signup/emailSendVerifyCheck";
        SignUpCertVerifyRequest certVerifyRequest = new SignUpCertVerifyRequest("test@naver.com", 123456);
        String request = objectMapper.writeValueAsString(certVerifyRequest);
        SignUpCertVerifyResponse response = new SignUpCertVerifyResponse(true);
        when(signCertService.emailVerifyCodeCheck(certVerifyRequest)).thenReturn(response);

        // when
        ResultActions actions = jsonWhen( "/cert/email/verify",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        documentResponse(),
                        requestFields(
                                fieldWithPath("send").type(JsonFieldType.STRING).description("이메이"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("true : 성공 , false: 실패")
                        )
                ));

        autoDoc(pathAdoc,
                errorcode(SIGNUP_EMAIL_VERIFY_CODE_FAILED));
    }

    private ResultActions jsonWhen(String uri, String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(defaultUri + uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request));
    }
}
