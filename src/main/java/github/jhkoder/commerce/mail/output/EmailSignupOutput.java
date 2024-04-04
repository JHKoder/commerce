package github.jhkoder.commerce.mail.output;

public class EmailSignupOutput {
    public static String signupText(int verificationCode) {
        return """
                        인증 번호 : [%d]

                        <br>
                        <br>
                        <br>
                        Home Page <a href ="">link</a><br>
                        DEV: <a href = "https://github.com/JHKoder">JHKoder</a>
                """.formatted(verificationCode);
    }
    public static String signupTitle(){
        return "[JHkoder E-commerce] 이메일 주소 인증";
    }
}
