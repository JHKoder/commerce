package github.jhkoder.commerce.image.repository;

import github.jhkoder.commerce.image.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Images, Long> {
}
