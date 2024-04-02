package github.jhkoder.commerce.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "서버 오류"),
    INVALID_VALUE(400, "유효하지 않은 값 입니다."),

    SECURITY_AUTHENTICATION_METHOD_NOT_SUPPORTED(450,"인증 방법이 지원되지 않습니다."),
    SECURITY_MESSAGE(451,""),
    IMAGE_REMOTE_UPLOAD(000,"" ), IMAGE_FILTER_NAME(000,"" );

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
