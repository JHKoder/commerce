package github.jhkoder.commerce.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum OrderStatus {
    STATUS_WAIT(-1, "주문 요청 준비중 입니다. "),
    STATUS_61_NUMBER(-61, "현금영수증 발행취소가 실패한 상태값입니다."),
    STATUS_60_NUMBER(-60, "현금영수증 발행이 실패한 상태값입니다."),
    STATUS_40_NUMBER(-40, "자동결제 빌링키 발급 실패 상태값입니다."),
    STATUS_11_NUMBER(-11, "자동결제 빌링키 발급 취소 상태값입니다."),
    STATUS_MINUS_2_NUMBER(-2, "결제 승인실패 오류가 발생된 상태값입니다."),
    STATUS_MINUS_4_NUMBER(-4, "결제 요청 실패가 발생된 상태값입니다."),
    STATUS_0_NUMBER(0, "결제 대기 상태입니다. PG사 결제창이 생성되었을때의 상태입니다."),
    STATUS_1_NUMBER(1, "결제완료된 상태입니다. 부분취소가 된 상태에서 전체금액이 취소되지 않았다면, 결제완료 상태입니다."),
    STATUS_2_NUMBER(2, "결제승인중 상태입니다. 클라이언트 승인, 서버 승인 전 상태입니다."),
    STATUS_4_NUMBER(4, "PG로 결제 승인 요청 상태 부트페이 내부적으로 사용되는 값"),
    STATUS_5_NUMBER(5, "가상계좌 발급완료 및 입금 대기 상태입니다."),
    STATUS_11_NUMBER_SECOND(11, "자동결제 빌링키 발급 완료 상태값입니다."),
    STATUS_12_NUMBER(12, "본인인증이 완료된 상태값입니다."),
    STATUS_20_NUMBER(20, "결제취소 완료상태입니다. 결제된 금액 전액이 취소되면, 결제취소 완료상태가 됩니다."),
    STATUS_40_NUMBER_SECOND(40, "자동결제 빌링키 발급 준비 상태입니다."),
    STATUS_41_NUMBER(41, "자동결제 빌링키 발급 이전 상태입니다. 생체인증, 비밀번호 결제시나리오에서 서버 승인 전 상태이기도 합니다."),
    STATUS_42_NUMBER(42, "자동결제 빌링키 발급 성공 상태값입니다."),
    STATUS_50_NUMBER(50, "본인인증 시작 준비 상태값입니다."),
    STATUS_60_NUMBER_SECOND(60, "현금영수증을 별도 발행시 현금영수증 발행 완료된 상태값입니다."),
    STATUS_61_NUMBER_SECOND(61, "현금영수증 별도 발행시 현금영수증 발행 취소가 완료된 상태값입니다.");

    private final int statusCode;
    private final String description;

    OrderStatus(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }
}
