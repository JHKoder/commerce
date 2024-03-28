package github.jhkoder.commerce.flatform.naver.ep.productcategory.domain;

import github.jhkoder.commerce.flatform.naver.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.naver.ep.product.domain.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
