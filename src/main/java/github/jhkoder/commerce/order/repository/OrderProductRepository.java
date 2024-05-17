package github.jhkoder.commerce.order.repository;

import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<List<OrderProduct>> findByOrder(Order order);

}
