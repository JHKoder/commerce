package github.jhkoder.commerce.flatform.naver.ep.category.domain;
import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false,length = 50)
    private String name;
}
