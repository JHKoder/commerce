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
@Table(name = "category")
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private CategoryLevel level;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> child  = new ArrayList<>();

    public Category(String name, CategoryLevel level) {
        this.name = name;
        this.level=level;
    }

    public void updateCategory(Category category){
        this.child.add(category);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void childChangeIndex(int index, int toIndex){
        Collections.swap(child, index,toIndex);
    }

    public boolean equalId(Long id) {
        return this.id.equals(id);
    }
    public void updateId(Long id){
        this.id = id;
    }
}
