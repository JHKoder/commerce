package github.jhkoder.commerce.flatform.local.ep.item.repository;

import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
