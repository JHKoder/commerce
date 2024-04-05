package github.jhkoder.commerce.exception;

import lombok.Getter;

import java.util.Arrays;

/**
 * 에러 코드
 */
@Getter
public enum ErrorCode {
    //Server
    INTERNAL_SERVER_ERROR(500, "서버 오류"),
    INVALID_VALUE(400, "유효하지 않은 값 입니다."),

    //security
    SECURITY_AUTHENTICATION_METHOD_NOT_SUPPORTED(450, "인증 방법이 지원되지 않습니다."),
    SECURITY_MESSAGE(451, ""),

    //image
    IMAGE_REMOTE_UPLOAD(401, "이미지 원격 업로드 실패"),
    IMAGE_REMOTE_SESSION(402, "이미지 세션 오류"),

    //USER
    USER_EMAIL_UNIQUE(401, "이메일 중복 에러"),
    USER_PHONE_UNIQUE(402, "휴대폰 중복 에러"),
    USER_ID_DUPLICATE(403, "아이디 중복 에러"),

    //email
    EMAIL_SEND_PARSE(401, "메시지 구문 분석에 실패"),
    EMAIL_SEND_AUTHENTICATION(402, "인증이 실패"),
    EMAIL_SEND(403, "메시지 전송에 실패"),

    //sms
    SMS_SEND_FAIL(401, "문자 전송 실패"),

    //signup
    SIGNUP_SMS_EXCEED(401, "회원가입 폰 재 인증 횟수 초과"),
    SIGNUP_SMS_VERIFY_CODE_FAILED(402, "회원가입 sms 문자 인증 실패"),
    SIGNUP_SMS_DUPLICATE(403, "회원가입 휴대폰번호가 중복되었습니다."),
    SIGNUP_EMAIL_EXCEED(404, "회원가입 이메일 재 인증 횟수 초과"),
    SIGNUP_EMAIL_VERIFY_CODE_FAILED(405, "회원가입 이메일 인증 실패"),
    SIGNUP_EMAIL_DUPLICATE(406, "회원가입 이메일이 중복되었습니다."),
    SIGNUP_CERT_CODE_UNVERIFIED(407, "회원가입 인증코드 미인증");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public static ErrorCode fromMessage(String message) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.getMessage().equals(message))
                .findFirst()
                .orElse(null);
    }
}
