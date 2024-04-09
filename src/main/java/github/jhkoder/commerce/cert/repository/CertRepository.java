package github.jhkoder.commerce.cert.repository;

import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.cert.domain.Cert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertRepository extends JpaRepository<Cert, Long> {

    Optional<List<Cert>> findByCertAuthenticationAndVerificationSentAndAuthentication(CertAuthentication certAuthentication, String email, OracleBoolean oracleBoolean);
    Optional<List<Cert>> findByVerificationSentAndAuthentication(String verificationSent, OracleBoolean authentication);

}
