package github.jhkoder.commerce.flatform.local.ep.category.repository;

import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
