package github.jhkoder.commerce.order.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 주문 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 구매자
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemProduct product; // 상품 정보

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 결제상태

    private int quantity; // 상품 수량  d
    private int productPrice;  // 상품 가격 d
    private int discountAmount; // 할인 금액
    private int vat; // 부가세
    private int totalPrice;  // 총 결제 금액 d
    private int installmentPeriod;  // 할부 기간
    private int cancelAmount; // 취소 금액
    private int supplyAmount; // 공급 가액
    private int exemptAmount; // 면세 금액
    private int actualPaymentAmount;// 실 결제 금액
    private boolean discounted;  // 할인 여부
    private String receiptId; // 영수중 ID String 암호 문
    private String pg;// PG (토스)
    private String paymentMethod; // 결제 방식 (카드)

    //card
    private String pgTid; // PG TID string
    private String cardCompany; // 카드사명
    private String cardNumber;  // 카드 번호
    private String approvalNumber; // 카드 승인 번호
    private String cardType;   // 카드 유형
    private String corporateOrIndividual; // 법인/개인

    private String zipCode;     //우편번호
    private String address; //주소
    private String detailedAddress; //상세주소
    private String referenceItems; //참고항목


    public static Order ofNew(ItemProduct itemProduct, User user, int quantity) {
        return new Order(user, itemProduct, OrderStatus.STATUS_WAIT, quantity);
    }

    private Order(User user, ItemProduct product, OrderStatus status, int quantity) {
        this.user = user;
        this.product = product;
        this.status = status;
        this.quantity = quantity;
    }

    public boolean equalsProductId(Long productId) {
        return product.getId().equals(productId);
    }
}