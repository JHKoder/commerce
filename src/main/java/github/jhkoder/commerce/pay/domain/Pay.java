package github.jhkoder.commerce.pay.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "pays")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class Pay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String receiptId;
    private String order_id;
    private int price;
    private int taxFree;
    private int cancelledPrice;
    private int cancelledTaxFree;
    private String order_name;
    private String company_name;
    private String gateway_url;
    private String sandbox;
    private String pg;
    private String method;
    private String method_symbol;
    private String method_origin;
    private String method_origin_symbol;
    private String currency;
    private String receipt_url;
    private String purchased_at;
    private String cancelled_at;
    private String requested_at;
    private String escrow_status_locale;
    private String escrow_status;
    private String status_locale;
    private int status;

    private String card_approve_no;
    private String card_no;
    private String card_quota;
    private String card_company_code;
    private String card_company;
    private String card_receipt_url; //영수중 확인

    private String phone_auth_no;
    private String phone;

    private String bank_code;
    private String bank_name;
    private String cash_receipt_no;
    private String sender_name;
}
