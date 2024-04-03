package github.jhkoder.commerce.signcert.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignCert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String sessionId;
    private String verificationCode;
    private String verificationSent;

    @Enumerated(value = EnumType.STRING)
    private SignUpCert signUpCert;

    public SignCert(String sessionId, String verificationCode, String verificationSent, SignUpCert signUpCert) {
        this.sessionId = sessionId;
        this.verificationCode = verificationCode;
        this.verificationSent = verificationSent;
        this.signUpCert = signUpCert;
    }
}
