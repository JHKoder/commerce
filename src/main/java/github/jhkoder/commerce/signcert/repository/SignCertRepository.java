package github.jhkoder.commerce.signcert.repository;

import github.jhkoder.commerce.signcert.domain.SignCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignCertRepository extends JpaRepository<SignCert, Long> {
    Optional<SignCert> findBySessionId(String sessionId);
}
