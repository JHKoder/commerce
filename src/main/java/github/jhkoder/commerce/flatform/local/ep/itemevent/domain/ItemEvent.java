package github.jhkoder.commerce.flatform.local.ep.itemevent.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@Table(name = "item_event")
@NoArgsConstructor(access = PROTECTED)
public class ItemEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemProduct product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;


    private int discountAmount;
    private String eventWords;
    private OracleBoolean coupon;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ItemEvent(ItemProduct product, Category category, int discountAmount, String eventWords, boolean coupon, LocalDateTime startTime, LocalDateTime endTime) {
        this.product = product;
        this.category = category;
        this.discountAmount = discountAmount;
        this.eventWords = eventWords;
        this.coupon = OracleBoolean.of(coupon);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void updateCoupon(boolean coupon){
        this.coupon = OracleBoolean.of(coupon);
    }
}
