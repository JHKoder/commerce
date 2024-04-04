package github.jhkoder.commerce.signcert.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.OracleBoolean;
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
    private int verificationCode;
    private String verificationSent;

    @Enumerated(value = EnumType.STRING)
    private SignCertAuthentication signCertAuthentication;
    @Enumerated(value = EnumType.STRING)
    private OracleBoolean authentication;

    public SignCert(int verificationCode, String verificationSent, SignCertAuthentication signCertAuthentication, OracleBoolean authentication) {
        this.verificationCode = verificationCode;
        this.verificationSent = verificationSent;
        this.signCertAuthentication = signCertAuthentication;
        this.authentication=authentication;
    }
}
