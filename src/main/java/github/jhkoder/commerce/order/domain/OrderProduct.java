package github.jhkoder.commerce.order.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "order_products")
@NoArgsConstructor(access = PROTECTED)
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemProduct product;

    private int quantity; // 상품 수량
    private int productPrice;  // 상품 가격
    private int discountAmount; // 할인 금액

    public OrderProduct(Order order, ItemProduct product, int quantity, int productPrice, int discountAmount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.discountAmount = discountAmount;
    }
}
