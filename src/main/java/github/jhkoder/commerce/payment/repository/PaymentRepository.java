package github.jhkoder.commerce.payment.repository;

import github.jhkoder.commerce.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
