package github.jhkoder.commerce.signcert.repository;

import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.signcert.domain.SignCert;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignCertRepository extends JpaRepository<SignCert, Long> {

    Optional<SignCert> findBySignCertAuthenticationAndVerificationSentAndAuthentication(SignCertAuthentication signCertAuthentication, String email, OracleBoolean oracleBoolean);
}
