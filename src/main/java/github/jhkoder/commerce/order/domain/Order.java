package github.jhkoder.commerce.order.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.payment.domain.Payment;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 구매자

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;
    private String receiptId; // 영수중 ID String 암호 문


    private String zipCode;     //우편번호
    private String address; //주소
    private String detailedAddress; //상세주소
    private String referenceItems; //참고항목


    public Order(User user) {
        this.user = user;
    }

    public void updatePay(Payment payment, String receiptId, String zipCode, String address, String detailedAddress, String referenceItems) {
        this.payment = payment;
        this.receiptId = receiptId;
        this.zipCode = zipCode;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.referenceItems = referenceItems;

    }
}