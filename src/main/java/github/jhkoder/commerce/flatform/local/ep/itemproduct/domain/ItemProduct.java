package github.jhkoder.commerce.flatform.local.ep.itemproduct.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "local_item_product")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ItemProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @OneToOne(fetch = FetchType.LAZY)
    private Item item;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Images> links = new ArrayList<>();


    private int price;
    private OracleBoolean orderMode;
    private String rentalInfo;
    private int clickCount;
    private int reviewCount;
    private int minimumPurchaseQuantity;
    private String optionDetail;
    private Gender gender;
    private int deliveryPrice;
    private String shippingSetting;
    private OracleBoolean fastDelivery;
    private OracleBoolean regularDelivery;
    private OracleBoolean dawnDelivery;
    private String isbn; // 도서 상품 코드
    private int stock;

    public ItemProduct(User user, Category category, Item item, List<Images> links, int price,
                       boolean orderMode, String rentalInfo, int clickCount, int reviewCount,
                       int minimumPurchaseQuantity, String optionDetail, Gender gender,
                       int deliveryPrice, String shippingSetting,
                       boolean fastDelivery, boolean regularDelivery, boolean dawnDelivery, String isbn, int stock) {
        this.user = user;
        this.category = category;
        this.item = item;
        this.links.addAll(links);
        this.price = price;
        this.orderMode = OracleBoolean.of(orderMode);
        this.rentalInfo = rentalInfo;
        this.clickCount = clickCount;
        this.reviewCount = reviewCount;
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
        this.optionDetail = optionDetail;
        this.gender = gender;
        this.deliveryPrice = deliveryPrice;
        this.shippingSetting = shippingSetting;
        this.fastDelivery = OracleBoolean.of(fastDelivery);
        this.regularDelivery = OracleBoolean.of(regularDelivery);
        this.dawnDelivery = OracleBoolean.of(dawnDelivery);
        this.isbn = isbn;
        this.stock = stock;
    }

    public void updateImages(List<Images> links){
        this.links.addAll(links);
    }

    public void updateAll( int price,
                           boolean orderMode, String rentalInfo, int clickCount, int reviewCount,
                           int minimumPurchaseQuantity, String optionDetail, Gender gender,
                           int deliveryPrice, String shippingSetting,
                           boolean fastDelivery, boolean regularDelivery, boolean dawnDelivery, String isbn, int stock){
        this.price = price;
        this.orderMode = OracleBoolean.of(orderMode);
        this.rentalInfo = rentalInfo;
        this.clickCount = clickCount;
        this.reviewCount = reviewCount;
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
        this.optionDetail = optionDetail;
        this.gender = gender;
        this.deliveryPrice = deliveryPrice;
        this.shippingSetting = shippingSetting;
        this.fastDelivery = OracleBoolean.of(fastDelivery);
        this.regularDelivery = OracleBoolean.of(regularDelivery);
        this.dawnDelivery = OracleBoolean.of(dawnDelivery);
        this.isbn = isbn;
        this.stock = stock;

    }

    public void updateMainImage(Images mainImage) {
        this.item.updateMainImage(mainImage);
    }
}
