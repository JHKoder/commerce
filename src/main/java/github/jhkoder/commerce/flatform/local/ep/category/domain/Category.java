package github.jhkoder.commerce.flatform.local.ep.category.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "local_category",indexes = @Index(name = "idx_category_title", columnList = "category_title"))
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_title", nullable = false)
    private String title;

    public Category(String title) {
        this.title = title;
    }
}
