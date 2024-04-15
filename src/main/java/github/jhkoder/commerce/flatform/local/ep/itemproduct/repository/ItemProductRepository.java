package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemProductRepository extends JpaRepository<ItemProduct, Long> {
}
