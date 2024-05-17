package github.jhkoder.commerce.payment.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 결제 번호
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 주문 정보
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 결제상태

    private int discountAmount; // 할인 금액
    private int vat; // 부가세
    private int actualPaymentAmount;// 실 결제 금액
    private boolean discounted;  // 할인 여부

    private String pg;// PG (토스)
    private String paymentMethod; // 결제 방식 (카드)

    //card
    private String pgTid; // PG TID string
    private String cardCompany; // 카드사명
    private String cardNumber;  // 카드 번호
    private String approvalNumber; // 카드 승인 번호
    private String cardType;   // 카드 유형
    private String corporateOrIndividual; // 법인/개인

    public Payment(Order order, OrderStatus status, int discountAmount, int vat, int actualPaymentAmount, boolean discounted, String pg, String paymentMethod, String pgTid, String cardCompany, String cardNumber, String approvalNumber, String cardType, String corporateOrIndividual, String zipCode, String address, String detailedAddress, String referenceItems) {
        this.order = order;
        this.status = status;
        this.discountAmount = discountAmount;
        this.vat = vat;
        this.actualPaymentAmount = actualPaymentAmount;
        this.discounted = discounted;
        this.pg = pg;
        this.paymentMethod = paymentMethod;
        this.pgTid = pgTid;
        this.cardCompany = cardCompany;
        this.cardNumber = cardNumber;
        this.approvalNumber = approvalNumber;
        this.cardType = cardType;
        this.corporateOrIndividual = corporateOrIndividual;
    }

}
