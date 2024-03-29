package github.jhkoder.commerce.flatform.naver.ep.productDelivery.domain;

import github.jhkoder.commerce.flatform.naver.ep.delivery.domain.Delivery;
import github.jhkoder.commerce.flatform.naver.ep.product.domain.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "product_delivery")
public class ProductDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;
}
