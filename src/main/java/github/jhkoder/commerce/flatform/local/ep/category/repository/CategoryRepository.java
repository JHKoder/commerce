package github.jhkoder.commerce.flatform.local.ep.category.repository;

import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByPathStartsWith(String path);

    Optional<List<Category>> findByLevel(CategoryLevel level);

    List<Category> findByParent(Category parent);
}
