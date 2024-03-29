package github.jhkoder.commerce.flatform.naver.ep.benefit.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "benefit")
public class Benefit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_event", length = 100)
    private String cardEvent; //카드명,카드 할인 가격
    @Column(name = "event_words", length = 100)
    private String eventWords; // 이벤트
    @Column(name = "coupon")
    private String coupon; //일반 /제휴쿠폰
    @Column(name = "partner_coupon_download", length = 1)
    private String partnerCouponDownload; //쿠폰다운로드 필요여부
    @Column(name = "interest_free_event", length = 100)
    private String interestFreeEvent; //카드무이자 할부 정보
    @Column(name = "point", length = 50)
    private String point;  //포인트  ex) 네이버포인트^400
}
