package github.jhkoder.commerce.exception;


import java.util.Arrays;

/**
 * 에러 코드
 */
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
    IMAGE_REMOTE_DELETE_FAIL(403, "이미지 삭제 실패"),
    IMAGE_NOT_ID(404,"이미지의 고유한 ID 가 존재하지 않음"),
    IMAGE_AUTHOR_MISMATCH(404,"이미지의 작성자가 일치하지 않음"),

    //USER
    USER_EMAIL_UNIQUE(401, "이메일 중복 에러"),
    USER_PHONE_UNIQUE(402, "휴대폰 중복 에러"),
    USER_ID_DUPLICATE(403, "아이디 중복 에러"),
    USER_NOT_FOUND(404,"유저 정보 없음"),
    USER_NOT_EMAIL(405,"이메일 정보 없음"),
    USER_NOT_PHONE(406,"휴대폰 정보 없음"),

    //email
    EMAIL_SEND_PARSE(401, "메시지 구문 분석에 실패"),
    EMAIL_SEND_AUTHENTICATION(402, "인증이 실패"),
    EMAIL_SEND(403, "메시지 전송에 실패"),

    //sms
    SMS_SEND_FAIL(401, "문자 전송 실패"),

    //signup
    SIGNUP_SMS_EXCEED(401, "회원가입 휴대폰 인증 횟수 초과"),
    SIGNUP_SMS_VERIFY_CODE_FAILED(402, "휴대폰 문자 인증 실패"),
    SIGNUP_SMS_DUPLICATE(403, "회원가입 휴대폰 번호 중복"),
    SIGNUP_EMAIL_EXCEED(404, "회원가입 이메일 인증 횟수 초과"),
    SIGNUP_EMAIL_VERIFY_CODE_FAILED(405, "이메일 인증 실패"),
    SIGNUP_EMAIL_DUPLICATE(406, "회원가입 이메일 중복"),
    SIGNUP_CERT_CODE_UNVERIFIED(407, "회원가입 인증코드 미인증"),

    ADMIN_NOT(401,"관리자가 아님"),
    ADMIN_TARGET_USER_NO_PERMISSION_REQUEST(402,"권한 신청자가 아님" ),
    CATEGORY_NOT_FOUND(401,"카테고리 정보 없음"),
    PRODUCT_NOT_FOUND(402,"상품 정보 없음"),
    PRODUCT_NOT_SELLER_USER(403,"아이템 조회 대상자와 일치 하지 않음"),
    CATEGORY_NOT_TOP_ID(402,"상위 카테고리 정보 없음" ),
    CATEGORY_CHANGE_NOT_MOVE(404,"카테고리 상한선 제한 으로 이동 실패" ),
    CATEGORY_MOVE_UP_INDEX_OUT(405,"카테고리 상단 이동 실패" ),
    CATEGORY_MOVE_DOWN_INDEX_OUT(406,"카테고리 하단 이동 실패" ),
    CATEGORY_NOT_TOP_EXIST(407,"카테고리가 상위에 존재하지 않습니다." ),
    COUPON_QR_REGISTRATION_LIMIT(401,"스토어 쿠폰 등록 제한" ),
    COUPON_CODE_NOT_FOUND(402,"쿠폰 코드 없음" ),
    COUPON_CODE_MISMATCH(403,"쿠폰 코드 비일치");

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

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
