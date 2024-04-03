package github.jhkoder.commerce.signcert.service;

import github.jhkoder.commerce.signcert.domain.SignCert;
import github.jhkoder.commerce.signcert.repository.SignCertDslRepository;
import github.jhkoder.commerce.signcert.repository.SignCertRepository;
import github.jhkoder.commerce.signcert.service.request.SignSmsCertRequest;
import github.jhkoder.commerce.signcert.service.request.SignUpCertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignCertService {
    private final SignCertDslRepository dslRepository;
    private final SignCertRepository Repository;

    @Transactional
    public SignCert saveCert(SignUpCertRequest request) {
        SignCert signCert = new SignCert(request.sessionId(), request.verificationCode(), request.verificationSent(), request.signUpCert());
        return Repository.save(signCert);
    }

    @Transactional(readOnly = true)
    public Optional<List<SignCert>> findBySend(SignSmsCertRequest request) {
        return dslRepository.findRecentSignCertsWithVerificationSent(request.sessionId(), request.verificationSent());
    }
}
