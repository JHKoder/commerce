package github.jhkoder.commerce.flatform.naver.ep.productBenefit.domain;

import github.jhkoder.commerce.flatform.naver.ep.benefit.domain.Benefit;
import github.jhkoder.commerce.flatform.naver.ep.product.domain.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "product_benefit")
public class ProductBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "benefit_id", nullable = false)
    private Benefit benefit;
}
