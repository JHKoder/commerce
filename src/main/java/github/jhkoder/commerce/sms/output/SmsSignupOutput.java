package github.jhkoder.commerce.sms.output;

public class SmsSignupOutput {

    public static String signupText(int verificationCode){
        return
                """
                [Web발송]
                
                인증 번호[%d]를 화면에 입력해주세요.
                (*발송자 한테는 표시 되지 않습니다.)
                """.formatted(verificationCode);
    }
}
