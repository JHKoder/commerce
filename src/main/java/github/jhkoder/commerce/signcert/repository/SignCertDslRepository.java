package github.jhkoder.commerce.signcert.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.signcert.domain.SignCert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static github.jhkoder.commerce.signcert.domain.QSignCert.signCert;

@Repository
@RequiredArgsConstructor
public class SignCertDslRepository {
    private final JPAQueryFactory factory;

    public Optional<List<SignCert>> findRecentSignCertsWithVerificationSent(String sessionId, String verificationSent) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return Optional.of(factory.selectFrom(signCert)
                .where(signCert.verificationSent.eq(verificationSent)
                        .and(signCert.createTime.after(oneDayAgo))
                        .and(signCert.sessionId.eq(sessionId))
                )
                .fetch());
    }
}
