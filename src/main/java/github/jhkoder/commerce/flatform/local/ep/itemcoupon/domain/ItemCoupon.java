package github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.domain.Images;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "local_item_Coupon")
@NoArgsConstructor(access = PROTECTED)
public class ItemCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemProduct product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    private Images qrImage;

    private String code;
    private int discountAmount;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ItemCoupon(ItemProduct product, Category category, int discountAmount, LocalDateTime startTime, LocalDateTime endTime) {
        this.product = product;
        this.category = category;
        this.discountAmount = discountAmount;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public void updateImage(Images image) {
        this.qrImage = image;
    }

    public void updateCode(String code) {
        this.code=code;
    }
}