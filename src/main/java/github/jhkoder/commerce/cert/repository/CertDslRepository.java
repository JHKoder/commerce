package github.jhkoder.commerce.cert.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.cert.domain.Cert;
import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static github.jhkoder.commerce.cert.domain.QCert.cert;

@Repository
@RequiredArgsConstructor
public class CertDslRepository {
    private final JPAQueryFactory factory;

    public Optional<Cert> findByVerificationSentAndVerificationCode(String verificationSent, int verificationCode, CertAuthentication authentication) {

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return Optional.ofNullable(factory.selectFrom(cert)
                .where(cert.verificationSent.eq(verificationSent)
                        .and(cert.certAuthentication.eq(authentication))
                        .and(cert.verificationCode.eq(verificationCode))
                        .and(cert.createTime.after(oneDayAgo))
                )
                .orderBy(cert.createTime.desc())
                .fetchFirst());
    }

    public int countByVerificationSent(String verificationSent, CertAuthentication auth) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return factory.selectFrom(cert)
                .where(cert.verificationSent.eq(verificationSent)
                        .and(cert.certAuthentication.eq(auth))
                        .and(cert.createTime.after(oneDayAgo))
                )
                .fetch().size();
    }

    @Transactional
    public void updateAuthenticationByCertAuthentication(CertAuthentication auth, OracleBoolean authentication, String sent){
        factory.update(cert)
                .set(cert.authentication,authentication)
                .where(cert.certAuthentication.eq(auth)
                        .and(cert.verificationSent.eq(sent)))
                .execute();
    }

}
