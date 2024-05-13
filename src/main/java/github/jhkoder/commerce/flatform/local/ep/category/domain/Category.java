package github.jhkoder.commerce.flatform.local.ep.category.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "category", indexes = {@Index(name = "idx_category_path", columnList = "path")})
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String path;

    @Column(name = "category_level")
    @Enumerated(value = EnumType.STRING)
    private CategoryLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> child = new ArrayList<>();

    public Category(String name, CategoryLevel level) {
        this.name = name;
        this.level = level;
    }

    public Category(String name, CategoryLevel level, Category parent) {
        this.name = name;
        this.level = level;
        this.parent = parent;
    }


    private Category(Long id) {
        this.id = id;
    }


    public static Category ofMeta(Long category) {
        return new Category(category);
    }

    public void updatePath(String path) {
        this.path = path;
    }

    public Long getParentId() {
        if (parent != null) {
            return parent.getId();
        }
        return -1L;
    }

    public boolean isParentId(Long id) {
        return getParentId().equals(id);
    }

    public void addChild(Category category) {
        this.child.add(category);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void childChangeIndex(int index, int toIndex) {
        Collections.swap(child, index, toIndex);
    }

    public void updateId(Long id) {
        this.id = id;
    }

    public boolean isTop() {
        return this.level == CategoryLevel.TOP;
    }
}
