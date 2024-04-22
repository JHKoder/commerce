package github.jhkoder.commerce.flatform.local.ep.itemcoupon.repository;

import github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.ItemCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCouponRepository extends JpaRepository<ItemCoupon, Long> {
    Optional<ItemCoupon> findByCode(String code);

}
