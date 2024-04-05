package github.jhkoder.commerce.user.web.rest;

import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.config.WebClientConfig;
import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import github.jhkoder.commerce.signcert.repository.SignCertDslRepository;
import github.jhkoder.commerce.signcert.repository.SignCertRepository;
import github.jhkoder.commerce.signcert.service.SignCertService;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;


public class SignUpApiControllerTest extends RestDocControllerTests {

    @MockBean
    private SignCertService signCertService;
    @MockBean
    private UserService userService;
    @MockBean
    private SmsService smsService;
    @MockBean
    private EmailService emailService;


    @Mock
    private UserRepository userRepository;
    @Mock
    private SignCertDslRepository signCertDslRepository;
    @Mock
    private SignCertRepository signCertRepository;
    @Mock
    private WebClientConfig webClientConfig;


    private final String defaultUri = "/api/signup";

    @Test
    void 회원가입() throws Exception {

        // given
        SignUpRequest signUpRequest = new SignUpRequest("userId", "userName", "password", "mail@email.com", "01012345678", User.Gender.MAN, SignCertAuthentication.EMAIL);

        String request = objectMapper.writeValueAsString(signUpRequest);

        doNothing().when(signCertService)
                .validateCert(any());
        doNothing().when(userService)
                .validateMemberRegistration(signUpRequest);
        doNothing().when(userService)
                .signup(any());

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/signup")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request));

        // then
        actions.andExpect(status().isOk())
                .andDo(document("signup",
                        documentRequest(),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 ").attributes(optional()),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰").attributes(optional()),
                                fieldWithPath("gender").type(User.Gender.MAN +","+ User.Gender.WOMAN).description("성별 타입 enum : [MAN],[WOMAN] "),
                                fieldWithPath("authenticationType").type(SignCertAuthentication.PHONE + "," + SignCertAuthentication.EMAIL).description("인증 타입 enum: [PHONE],[EMAIL] ")
                        )
                ));
    }
}
