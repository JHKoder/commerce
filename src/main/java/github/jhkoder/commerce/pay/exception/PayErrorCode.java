package github.jhkoder.commerce.pay.exception;

import lombok.Getter;

@Getter
public enum PayErrorCode {
    BAD_REQUEST(400,"보내는 파라메터 정보가 없거나 잘못된 경우"),
    UNAUTHORIZED(401,"다른 프로젝트의 결제건을 조회하려고 했거나 사용권한이 없는 API 인 경우"),
    NOT_FOUND(404,"지원하지 않는 URL"),
    SERVER_ERROR(500,"결제 진행중 부트페이 내부 오류가 발생했거나 PG사에서 오류가 발생한 경우"),
    OK(200,"정상적으로 API를 수행한 경우"),
    CREATED(201,"단 시간내 중복 요청으로 이미 수행된 내용을 다시한번 수행했을 경우. 부트페이 내부적으로 사용하므로 가맹점은 200 OK를 리턴 받습니다.");

    private final int errorCode;
    private final String massage;

    PayErrorCode(int errorCode, String massage) {
        this.errorCode = errorCode;
        this.massage = massage;
    }

}
