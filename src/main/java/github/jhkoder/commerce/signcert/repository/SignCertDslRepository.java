package github.jhkoder.commerce.signcert.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.signcert.domain.SignCert;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static github.jhkoder.commerce.signcert.domain.QSignCert.signCert;

@Repository
@RequiredArgsConstructor
public class SignCertDslRepository {
    private final JPAQueryFactory factory;

    public Optional<SignCert> findByVerificationSentAndVerificationCode(String verificationSent, int verificationCode, SignCertAuthentication authentication) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return Optional.ofNullable(factory.selectFrom(signCert)
                .where(signCert.verificationSent.eq(verificationSent)
                        .and(signCert.signCertAuthentication.eq(authentication))
                        .and(signCert.verificationCode.eq(verificationCode))
                        .and(signCert.createTime.after(oneDayAgo))
                )
                .orderBy(signCert.createTime.desc())
                .fetchFirst());
    }

    public int countByVerificationSent(String verificationSent, SignCertAuthentication auth) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return factory.selectFrom(signCert)
                .where(signCert.verificationSent.eq(verificationSent)
                        .and(signCert.signCertAuthentication.eq(auth))
                        .and(signCert.createTime.after(oneDayAgo))
                )
                .fetch().size();
    }

    @Transactional
    public void updateAuthenticationBySignCertAuthentication(SignCertAuthentication auth, OracleBoolean authentication,String sent){
        factory.update(signCert)
                .set(signCert.authentication,authentication)
                .where(signCert.signCertAuthentication.eq(auth)
                        .and(signCert.verificationSent.eq(sent)))
                .execute();
    }

}
