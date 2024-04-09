package github.jhkoder.commerce.flatform.local.ep.itemproduct.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "local_item_product")
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Images> links = new ArrayList<>();


    private int price;
    private int normalPrice;
    private OracleBoolean orderMode;
    private OracleBoolean brandCertification;
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
    private int stock;
    private String isbn; // 도서 상품 코드
}
