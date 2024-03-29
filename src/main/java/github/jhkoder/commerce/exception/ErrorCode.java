package github.jhkoder.commerce.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "서버 오류"),
    INVALID_VALUE(400, "유효하지 않은 값 입니다."),

    NOT_FOUND_USER(400, "유저 정보를 찾을 수 없습니다."),
    NOT_FOUND_NICKNAME(400, "존재하지 않는 닉네임 입니다."),
    USERS_ALREADY_PRESENT(400, "이미 있는 사용자 입니다."),


    VALIDATE_AUTHENTICATION(401, "RNJS"),
    NOT_FOUND_GROUP_TARGET_EMAIL_USER(400, "그룹에 포함된 유저를 찾지 못했습니다."),
    NOT_FOUND_TARGET_EMAIL_USER(400, "해당 이메일로 유저를 찾지 못햇습니다."),

    SECURITY_AUTHENTICATION_METHOD_NOT_SUPPORTED(450,"인증 방법이 지원되지 않습니다."),
    SECURITY_MESSAGE(451,"");

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
