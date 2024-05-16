package github.jhkoder.commerce.order.repository;

import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByUser(User user);

}
